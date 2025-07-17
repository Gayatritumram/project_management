package com.backend.project_management.ServiceImp;

import com.backend.project_management.DTO.MonthlyTaskCountDTO;
import com.backend.project_management.DTO.TaskCountDTO;
import com.backend.project_management.DTO.TaskDTO;
import com.backend.project_management.DTO.TaskSummaryDTO;
import com.backend.project_management.Entity.*;
import com.backend.project_management.Exception.RequestNotFound;
import com.backend.project_management.Mapper.TaskMapper;
import com.backend.project_management.Repository.BranchAdminRepository;
import com.backend.project_management.Repository.TaskRepository;
import com.backend.project_management.Repository.TeamLeaderRepository;
import com.backend.project_management.Repository.TeamMemberRepository;
import com.backend.project_management.Service.TaskService;
import com.backend.project_management.Pagination.TaskSpecification;
import com.backend.project_management.Util.JwtHelper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.time.LocalDate;
import java.util.*;
import java.util.stream.Collectors;

@Service
public class TaskServiceImpl implements TaskService {

    @Autowired private TaskRepository taskRepository;
    @Autowired private TaskMapper taskMapper;
    @Autowired private S3Service s3Service;

    @Autowired private TeamLeaderRepository teamLeaderRepository;
    @Autowired private TeamMemberRepository teamMemberRepository;
    @Autowired private JwtHelper jwtHelper;
    @Autowired
    private StaffValidation  staffValidation;

    @Autowired
    private BranchAdminRepository adminRepo;

    @Autowired
    private TeamMemberRepository repository;



    @Override
    public TaskDTO createTask(TaskDTO taskDTO, String token, Long id, MultipartFile file, String role, String email) throws IOException {

        System.out.println("Checking permission for role: " + role + ", email: " + email);
        if (!staffValidation.hasPermission(role, email, "POST")) {
            System.out.println("Permission denied!");
            throw new AccessDeniedException("No permission to createTask");
        }
        System.out.println("Permission granted!");

        Task task = taskMapper.toEntity(taskDTO);

        String branchCode = switch (role) {
            case "BRANCH" -> adminRepo.findByBranchEmail(email)
                    .orElseThrow(() -> new RequestNotFound("Admin not found"))
                    .getBranchCode();
            case "TEAM_LEADER" -> teamLeaderRepository.findByEmail(email)
                    .orElseThrow(() -> new RequestNotFound("Team Leader not found"))
                    .getBranchCode();
            default -> staffValidation.fetchBranchCodeByRole(role, email);
        };
        System.out.println("Fetched branchCode: " + branchCode);

        task.setRole(role);
        task.setCreatedByEmail(email);
        task.setBranchCode(branchCode);


        try {
            if (role.contains("BRANCH")) {
                BranchAdmin currentAdmin = adminRepo.findByBranchEmail(email)
                        .orElseThrow(() -> new RuntimeException("Logged-in admin not found"));
                System.out.println("currentAdmin: " + currentAdmin);
                task.setAssignedByAdmin(currentAdmin);
            } else if (role.contains("TEAM_LEADER")) {
                TeamLeader currentLeader = teamLeaderRepository.findByEmail(email)
                        .orElseThrow(() -> new RuntimeException("Logged-in team leader not found"));
                System.out.println("currentLeader: " + currentLeader);
                task.setAssignedByLeader(currentLeader);

            }
        } catch (RuntimeException e) {
            System.err.println("Warning: " + e.getMessage()); // log and continue
        }

        TeamMember assignedTo = teamMemberRepository.findById(id)
                    .orElseThrow(() -> new RuntimeException("Team member not found"));
        task.setAssignedToTeamMember(assignedTo);



        if (file != null && !file.isEmpty()) {
           try {
                task.setImageUrl(s3Service.uploadImage(file));
          } catch (IOException e) {
                throw new RuntimeException(e);
           }
        }

        TeamMember member = teamMemberRepository.findById(id).orElseThrow(() -> new RuntimeException("Task with ID " + id + " not found"));
        task.setAssignedToName(member.getName());

        Task savedTask = taskRepository.save(task);
        return taskMapper.toDto(savedTask);
    }


    @Override
    public TaskDTO updateTask(Long taskId, TaskDTO taskDTO, String role, String email, MultipartFile file) throws IOException {
        if (!staffValidation.hasPermission(role, email, "PUT")) {
            throw new AccessDeniedException("You do not have permission to update task");
        }

        Task task = taskRepository.findById(taskId)
                .orElseThrow(() -> new RuntimeException("Task with ID " + taskId + " not found"));

        if(role.equals("TEAM_MEMBER")){
            if (taskDTO.getStatus() != null) task.setStatus(taskDTO.getStatus());
            if (taskDTO.getStatusBar() != null) task.setStatusBar(taskDTO.getStatusBar());
        }else {

            // Conditionally update fields
            if (taskDTO.getSubject() != null) task.setSubject(taskDTO.getSubject());
            if (taskDTO.getDescription() != null) task.setDescription(taskDTO.getDescription());
            if (taskDTO.getProjectName() != null) task.setProjectName(taskDTO.getProjectName());
            if (taskDTO.getPriority() != null) task.setPriority(taskDTO.getPriority());
            if (taskDTO.getStatus() != null) task.setStatus(taskDTO.getStatus());
            if (taskDTO.getStatusBar() != null) task.setStatusBar(taskDTO.getStatusBar());
            if (taskDTO.getBranch() != null) task.setBranch(taskDTO.getBranch());
            if (taskDTO.getDepartment() != null) task.setDepartment(taskDTO.getDepartment());
            if (taskDTO.getDays() != 0) task.setDays(taskDTO.getDays());

            if (taskDTO.getStartDate() != null) task.setStartDate(taskDTO.getStartDate());
            if (taskDTO.getEndDate() != null) task.setEndDate(taskDTO.getEndDate());

            if (taskDTO.getAssignedToTeamMember() != null) {
                TeamMember assignedTo = teamMemberRepository.findById(taskDTO.getAssignedToTeamMember())
                        .orElseThrow(() -> new RuntimeException("Assigned team member not found"));
                task.setAssignedToTeamMember(assignedTo);
            }

            // File (image) update
            if (file != null && !file.isEmpty()) {
                String imageUrl = s3Service.uploadImage(file);
                task.setImageUrl(imageUrl);
            }

            // Keep original creator info if null
            if (task.getCreatedByEmail() == null) task.setCreatedByEmail(email);
            if (task.getRole() == null) task.setRole(role);
            if (task.getBranchCode() == null) {
                String branchCode = staffValidation.fetchBranchCodeByRole(role, email);
                task.setBranchCode(branchCode);
            }

            if ("TEAM_LEADER".equalsIgnoreCase(role) && task.getAssignedByLeader() == null) {
                TeamLeader leader = teamLeaderRepository.findByEmail(email)
                        .orElseThrow(() -> new RuntimeException("Team Leader not found"));
                task.setAssignedByLeader(leader);
            }
        }
        Task updatedTask = taskRepository.save(task);
        return taskMapper.toDto(updatedTask);
    }


    @Override
    public TaskDTO getTaskById(Long taskId, String role, String email) {
        if (!staffValidation.hasPermission(role, email, "GET")) {
            throw new AccessDeniedException("You do not have permission to view task");
        }
        Task task = taskRepository.findById(taskId)
                .orElseThrow(() -> new RuntimeException("Task not found"));
        return taskMapper.toDto(task);
    }


    @Override
    public List<TaskDTO> getAllTasks(String role, String email, String branchCode) {
        if (!staffValidation.hasPermission(role, email, "GET")) {
            throw new AccessDeniedException("You do not have permission to view task");
        }
        return taskRepository.findAllByBranchCode(branchCode).stream()
                .map(taskMapper::toDto)
                .collect(Collectors.toList());
    }

    @Override
    public void deleteTask(Long taskId, String role, String email) {
        if (!staffValidation.hasPermission(role, email, "DELETE")) {
            throw new AccessDeniedException("You do not have permission to view task");
        }
        Task task = taskRepository.findById(taskId)
                .orElseThrow(() -> new RuntimeException("Task not found"));
        taskRepository.delete(task);
    }

    @Override
    public List<TaskDTO> getTasksAssignedToMemberById(Long id, String role, String email) {
        if (!staffValidation.hasPermission(role, email, "GET")) {
            throw new AccessDeniedException("You do not have permission to view task");
        }
        List<Task> tasks = taskRepository.findAllByAssignedToTeamMember_Id(id);
        return taskMapper.toDtoList(tasks);
    }

    @Override
    public List<TaskDTO> getTasksAssignedByLeaderId(Long id, String role, String email) {
        if (!staffValidation.hasPermission(role, email, "GET")) {
            throw new AccessDeniedException("You do not have permission to view task");
        }
        List<Task> tasks = taskRepository.findAllByAssignedByLeader_Id(id);
        return taskMapper.toDtoList(tasks);
    }

    @Override
    public List<TaskDTO> getTasksAssignedByAdminId(Long id, String role, String email) {
        if (!staffValidation.hasPermission(role, email, "GET")) {
            throw new AccessDeniedException("You do not have permission to view task");
        }
        List<Task> tasks = taskRepository.findAllByAssignedByAdmin_Id(id);
        return taskMapper.toDtoList(tasks);
    }

    @Override
    public List<TaskDTO> getTasksAssignedByAdminEmail(String emailFind, String role,String email) {
        if (!staffValidation.hasPermission(role, email, "GET")) {
            throw new AccessDeniedException("You do not have permission to view task");
        }
        List<Task> tasks = taskRepository.findAllByAssignedByAdminEmail(emailFind);
        return taskMapper.toDtoList(tasks);
    }

    @Override
    public List<TaskDTO> getTasksAssignedByLeaderEmail(String emailFind,String role,String email) {
        if (!staffValidation.hasPermission(role, email, "GET")) {
            throw new AccessDeniedException("You do not have permission to view task");
        }
        List<Task> tasks = taskRepository.findAllByAssignedByLeaderEmail(emailFind);
        return taskMapper.toDtoList(tasks);
    }

    @Override
    public List<TaskDTO> getTasksAssignedToMemberEmail(String emailFind,String role,String email) {
        if (!staffValidation.hasPermission(role, email, "GET")) {
            throw new AccessDeniedException("You do not have permission to view task");
        }
        List<Task> tasks = taskRepository.findAllByAssignedToTeamMemberEmail(emailFind);
        return taskMapper.toDtoList(tasks);
    }

    @Override
    public List<TaskDTO> getTodaysLeaderTasksByEmail(String emailFind,String email, String role) {
        if (!staffValidation.hasPermission(role, email, "GET")) {
            throw new AccessDeniedException("You do not have permission to view task");
        }
        List<Task> tasks = taskRepository.findTodaysTasksAssignedToLeader(emailFind);
        return tasks.stream().map(taskMapper::toDto).collect(Collectors.toList());
    }

    @Override
    public TaskDTO createTaskForLeader(TaskDTO taskDTO, String token, Long id, MultipartFile file, String role, String email) throws IOException {
        System.out.println("Checking permission for role: " + role + ", email: " + email);
        if (!staffValidation.hasPermission(role, email, "POST")) {
            System.out.println("Permission denied!");
            throw new AccessDeniedException("No permission to createTask");
        }
        System.out.println("Permission granted!");

        Task task = taskMapper.toEntity(taskDTO);

        String branchCode = staffValidation.fetchBranchCodeByRole(role, email);
        System.out.println("Fetched branchCode: " + branchCode);

        task.setRole(role);
        task.setCreatedByEmail(email);
        task.setBranchCode(branchCode);

        try {
            if (role.contains("BRANCH")) {
                BranchAdmin currentAdmin = adminRepo.findByBranchEmail(email)
                        .orElseThrow(() -> new RuntimeException("Logged-in admin not found"));
                task.setAssignedByAdmin(currentAdmin);
            } else if (role.contains("TEAM_LEADER")) {
                TeamLeader currentLeader = teamLeaderRepository.findByEmail(email)
                        .orElseThrow(() -> new RuntimeException("Logged-in team leader not found"));
                task.setAssignedByLeader(currentLeader);
            }
        } catch (RuntimeException e) {
            System.err.println("Warning: " + e.getMessage()); // log and continue
        }

        TeamLeader assignedTo = teamLeaderRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Team Leader not found"));

        task.setAssignedToTeamLeader(assignedTo);

        if (file != null && !file.isEmpty()) {
            task.setImageUrl(s3Service.uploadImage(file));
        }
        ///
        TeamLeader member = teamLeaderRepository.findById(id).orElseThrow(() -> new RuntimeException("Task with ID " + id + " not found"));
        task.setAssignedToName(member.getName());

        Task savedTask = taskRepository.save(task);
        return taskMapper.toDto(savedTask);

    }

    @Override
    public List<TaskDTO> getTasksAssignedByAdminToMember(Long adminId, Long memberId, String role, String email) {
        if (!staffValidation.hasPermission(role, email, "GET")) {
            throw new AccessDeniedException("You do not have permission to view task");
        }

        List<Task> tasks = taskRepository.findTasksAssignedByAdminToMember(adminId, memberId);
        return taskMapper.toDtoList(tasks);
    }

    @Override
    public List<TaskDTO> getTodaysTasksAssignedByAdminToMember(Long adminId, Long memberId, String role, String email) {
        if (!staffValidation.hasPermission(role, email, "GET")) {
            throw new AccessDeniedException("You do not have permission to view task");
        }
        List<Task> tasks = taskRepository.findTodaysTasksAssignedByAdminToMember(adminId, memberId);
        return taskMapper.toDtoList(tasks);
    }

    @Override
    public List<TaskDTO> getTasksAssignedByLeaderToMember(Long leaderId, Long memberId, String role, String email) {
        if (!staffValidation.hasPermission(role, email, "GET")) {
            throw new AccessDeniedException("You do not have permission to view task");
        }
        List<Task> tasks = taskRepository.findTasksAssignedByLeaderToMember(leaderId, memberId);
        return taskMapper.toDtoList(tasks);
    }

    @Override
    public List<TaskDTO> getTodaysTasksAssignedByLeaderToMember(Long leaderId, Long memberId, String role, String email) {
        if (!staffValidation.hasPermission(role, email, "GET")) {
            throw new AccessDeniedException("You do not have permission to view task");
        }
        List<Task> tasks = taskRepository.findTodaysTasksAssignedByLeaderToMember(leaderId, memberId);
        return taskMapper.toDtoList(tasks);
    }

    @Override
    public TaskDTO assignTaskFromLeaderToMember(TaskDTO taskDTO, String token, Long leaderId, Long memberId, MultipartFile file, String role, String email) throws IOException {
        System.out.println("Checking permission for role: " + role + ", email: " + email);
        if (!staffValidation.hasPermission(role, email, "POST")) {
            System.out.println("Permission denied!");
            throw new AccessDeniedException("No permission to createTask");
        }
        System.out.println("Permission granted!");

        Task task = taskMapper.toEntity(taskDTO);

        String branchCode = staffValidation.fetchBranchCodeByRole(role, email);
        System.out.println("Fetched branchCode: " + branchCode);

        task.setRole(role);
        task.setCreatedByEmail(email);
        task.setBranchCode(branchCode);

        if (!role.contains("TEAM_LEADER")) {
            throw new RuntimeException("You must be a team leader to assign tasks to members");
        }

        // Find the team leader
        TeamLeader teamLeader = teamLeaderRepository.findById(leaderId)
                .orElseThrow(() -> new RuntimeException("Team leader not found"));

        // Verify the authenticated user is the same leader who's assigning the task
        if (!teamLeader.getEmail().equals(email)) {
            throw new RuntimeException("You can only assign tasks on your own behalf");
        }

        // Find the team member
        TeamMember teamMember = teamMemberRepository.findById(memberId)
                .orElseThrow(() -> new RuntimeException("Team member not found"));

        // Set the assigner and assignee
        task.setAssignedByLeader(teamLeader);
        task.setAssignedToTeamMember(teamMember);

        // Handle file attachment if provided
        if (file != null && !file.isEmpty()) {
            try {
                task.setImageUrl(s3Service.uploadImage(file));
            } catch (IOException e) {
                throw new RuntimeException("Failed to upload file attachment", e);
            }
        }

        // Save the task
        Task savedTask = taskRepository.save(task);
        return taskMapper.toDto(savedTask);

    }

    @Override
    public List<TaskDTO> getTasksAssignedToLeaderId(Long id, String role, String email) {
        if (!staffValidation.hasPermission(role, email, "GET")) {
            throw new AccessDeniedException("You do not have permission to view task");
        }
        List<Task> tasks = taskRepository.findAllByAssignedToTeamLeader_Id(id);
        return taskMapper.toDtoList(tasks);
    }

    @Override
    public List<TaskDTO> getAllTasks(String role, String email) {
        if (!staffValidation.hasPermission(role, email, "GET")) {
            throw new AccessDeniedException("You do not have permission to view tasks");
        }

        List<Task> tasks = taskRepository.findAll();
        return taskMapper.toDtoList(tasks);
    }


    @Override
    public Page<TaskDTO> getAllTasksWithFilter(TaskDTO filter, int page, int size, String sortBy, String sortType, String role, String email,String branchCode) {
        if (!staffValidation.hasPermission(role, email, "GET")) {
            throw new AccessDeniedException("No permission to view tasks");
        }

        Sort sort = sortType.equalsIgnoreCase("desc") ? Sort.by(sortBy).descending() : Sort.by(sortBy).ascending();
        Pageable pageable = PageRequest.of(page, size, sort);

        Page<Task> taskPage = taskRepository.findAll(TaskSpecification.build(filter), pageable);

        return taskPage.map(taskMapper::toDto); // assuming you have this method
    }

    @Override
    public List<TaskSummaryDTO> getAllTaskSummaries(String role,String email,String branchCode) {
        if (!staffValidation.hasPermission(role, email, "GET")) {
            throw new AccessDeniedException("No permission to view tasks");
        }
        List<Task> tasks = taskRepository.findAllByBranchCode(branchCode);
        return tasks.stream()
                .map(TaskMapper::mapToTaskSummary)
                .toList();
    }

    @Override
    public List<MonthlyTaskCountDTO> getMonthlyTaskCounts(int month, int year, String role, String email) {
        if (!staffValidation.hasPermission(role, email, "GET")) {
            throw new AccessDeniedException("Access Denied");
        }

        // Get number of days in the month
        LocalDate date = LocalDate.of(year, month, 1);
        int daysInMonth = date.lengthOfMonth();

        // Fetch counts only for existing days
        List<Object[]> dbResults = taskRepository.getTaskCountGroupedByDay(month, year);
        Map<Integer, Long> dayToCountMap = new HashMap<>();

        for (Object[] row : dbResults) {
            Integer day = (Integer) row[0];
            Long count = (Long) row[1];
            dayToCountMap.put(day, count);
        }

        // Fill all days with 0 if missing
        List<MonthlyTaskCountDTO> fullMonth = new ArrayList<>();
        for (int i = 1; i <= daysInMonth; i++) {
            long count = dayToCountMap.getOrDefault(i, 0L);
            fullMonth.add(new MonthlyTaskCountDTO(i, count));
        }

        return fullMonth;
    }


    @Override
    public Map<String, Long> getTaskCountsByTimeFrame(String branchCode, String role, String email) {
        if (!staffValidation.hasPermission(role, email, "GET")) {
            throw new AccessDeniedException("Access denied");
        }

        LocalDate today = LocalDate.now();
        LocalDate startOfToday = today; // For clarity
        LocalDate endOfToday = today;

        LocalDate last7Days = today.minusDays(7);
        LocalDate last30Days = today.minusDays(30);
        LocalDate last365Days = today.minusDays(365);

        long total = taskRepository.countByBranchCode(branchCode);
        long countToday = taskRepository.countByStartDateBetweenAndBranchCode(startOfToday, endOfToday, branchCode);
        long count7 = taskRepository.countByStartDateBetweenAndBranchCode(last7Days, today, branchCode);
        long count30 = taskRepository.countByStartDateBetweenAndBranchCode(last30Days, today, branchCode);
        long count365 = taskRepository.countByStartDateBetweenAndBranchCode(last365Days, today, branchCode);

        Map<String, Long> response = new LinkedHashMap<>();
        response.put("Total", total);
        response.put("Today", countToday);
        response.put("Last 7 Days", count7);
        response.put("Last 30 Days", count30);
        response.put("Last 365 Days", count365);

        return response;
    }

    @Override
    public List<Map<String, Object>> getTasksAssignedByLeaderToMembers(String role, String email) {
        if (!staffValidation.hasPermission(role, email, "GET")) {
            throw new AccessDeniedException("Access denied");
        }

        List<Task> tasks = taskRepository.findAllByAssignedByLeaderIsNotNullAndAssignedToTeamMemberIsNotNull();

        return tasks.stream().map(task -> {
            Map<String, Object> map = new LinkedHashMap<>();
            map.put("subject", task.getSubject());
            map.put("status", task.getStatus());
            map.put("description", task.getDescription());
            map.put("priority", task.getPriority());
            map.put("startDate", task.getStartDate());
            map.put("endDate", task.getEndDate());
            map.put("assignedToMemberName", task.getAssignedToName());
            return map;
        }).collect(Collectors.toList());
    }

    @Override
    public List<Map<String, Object>> getTasksAssignedByAdmin(String role, String email) {
        if (!staffValidation.hasPermission(role, email, "GET")) {
            throw new AccessDeniedException("Access denied");
        }

        List<Task> tasks = taskRepository.findAllByAssignedByAdminIsNotNull();

        return tasks.stream().map(task -> {
            Map<String, Object> map = new LinkedHashMap<>();
            map.put("subject", task.getSubject());
            map.put("status", task.getStatus());
            map.put("description", task.getDescription());
            map.put("priority", task.getPriority());
            map.put("startDate", task.getStartDate());
            map.put("endDate", task.getEndDate());

            if (task.getAssignedToTeamMember() != null) {
                map.put("assignedTo", "Member: " + task.getAssignedToTeamMember().getName());
            } else if (task.getAssignedToTeamLeader() != null) {
                map.put("assignedTo", "Leader: " + task.getAssignedToTeamLeader().getName());
            } else {
                map.put("assignedTo", "Unassigned");
            }

            return map;
        }).collect(Collectors.toList());
    }





}