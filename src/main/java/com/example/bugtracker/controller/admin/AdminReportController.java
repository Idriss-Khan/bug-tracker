package com.example.bugtracker.controller.admin;

import com.example.bugtracker.model.User;
import com.example.bugtracker.service.BugService;
import com.example.bugtracker.service.NotificationService;
import com.example.bugtracker.service.ProjectService;
import com.example.bugtracker.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.servlet.ModelAndView;

import java.security.Principal;
import java.time.LocalDate;
import java.util.List;
import java.util.Map;


/**
 * Rest controller for handling the admin report page.
 */
@RestController
@RequestMapping("/admin/report")
public class AdminReportController {

    @Autowired
    private UserService userService;
    @Autowired
    private NotificationService notificationService;
    @Autowired
    private ProjectService projectService;

    @Autowired
    private BugService bugService;

    // Returns reports page

    @GetMapping
    public ModelAndView getAdminReportPage(ModelAndView modelAndView, Principal principal) {
        modelAndView.setViewName("admin/report/report");
        modelAndView.addObject("pageTitle", "Reports");

        String email = principal.getName();
        User currentUser = userService.getUserByEmail(email);
        modelAndView.addObject("currentUser", currentUser);

        int notificationCount = notificationService.countUnreadNotifications(currentUser);
        modelAndView.addObject("notificationCount", notificationCount);

        // Populate Graph data
        populateGraphData(modelAndView);

        // Populate Project Severity Data
        populateProjectSeverityData(modelAndView);

        // Populate Bug Severity Data
        populateBugSeverityData(modelAndView);

        // Populate Bug Status Data
        populateBugStatusData(modelAndView);

        // Populate percentage change between months
        populateBugCreationChange(modelAndView);

        // Populate line graph with created date
        populateBugReportedLine(modelAndView);

        // Populate bar chart with Project bugs by priority
        populateBugBarChart(modelAndView);

        return modelAndView;
    }

    private void populateGraphData(ModelAndView modelAndView) {
        long totalProjects = projectService.countTotalProjects();
        long activeProjects = projectService.countActiveProjects();
        long closedProjects = projectService.countClosedProjects();
        int totalBugs = bugService.getTotalBugCount();

        modelAndView.addObject("totalProjects", totalProjects);
        modelAndView.addObject("activeProjects", activeProjects);
        modelAndView.addObject("closedProjects", closedProjects);
        modelAndView.addObject("totalBugs", totalBugs);
    }

    private void populateProjectSeverityData(ModelAndView modelAndView) {
        long highSeverityProjects = projectService.countProjectsBySeverity("High");
        long mediumSeverityProjects = projectService.countProjectsBySeverity("Medium");
        long lowSeverityProjects = projectService.countProjectsBySeverity("Low");

        modelAndView.addObject("highSeverityProjects", highSeverityProjects);
        modelAndView.addObject("mediumSeverityProjects", mediumSeverityProjects);
        modelAndView.addObject("lowSeverityProjects", lowSeverityProjects);
    }

    private void populateBugSeverityData(ModelAndView modelAndView) {
        long highSeverityBugs = bugService.countBugBySeverity("High");
        long mediumSeverityBugs = bugService.countBugBySeverity("Medium");
        long lowSeverityBugs = bugService.countBugBySeverity("Low");

        modelAndView.addObject("highSeverityBugs", highSeverityBugs);
        modelAndView.addObject("mediumSeverityBugs", mediumSeverityBugs);
        modelAndView.addObject("lowSeverityBugs", lowSeverityBugs);
    }

    private void populateBugStatusData(ModelAndView modelAndView) {
        long newStatusBugs = bugService.countBugByStatus("New");
        long inProgressStatusBugs = bugService.countBugByStatus("In-Progress");
        long resolvedStatusBugs = bugService.countBugByStatus("Resolved");
        long closedStatusBugs = bugService.countBugByStatus("Closed");

        modelAndView.addObject("newStatusBugs", newStatusBugs);
        modelAndView.addObject("inProgressStatusBugs", inProgressStatusBugs);
        modelAndView.addObject("resolvedStatusBugs", resolvedStatusBugs);
        modelAndView.addObject("closedStatusBugs", closedStatusBugs);
    }

    private void populateBugCreationChange(ModelAndView modelAndView){
        LocalDate today = LocalDate.now();
        LocalDate firstDayOfThisMonth = today.withDayOfMonth(1);
        LocalDate lastDayOfLastMonth = firstDayOfThisMonth.minusDays(1);
        LocalDate firstDayOfLastMonth = lastDayOfLastMonth.withDayOfMonth(1);

        // Calculate the number of bugs created this month and last month
        int bugsCreatedThisMonth = bugService.countBugsCreatedBetweenDates(firstDayOfThisMonth, today);
        int bugsCreatedLastMonth = bugService.countBugsCreatedBetweenDates(firstDayOfLastMonth, lastDayOfLastMonth);

        modelAndView.addObject("bugsCreatedThisMonth", bugsCreatedThisMonth);
        modelAndView.addObject("bugsCreatedLastMonth", bugsCreatedLastMonth);
    }

    private void populateBugReportedLine(ModelAndView modelAndView){
        Map<LocalDate, Long> bugCounts = bugService.getBugCountsOverTime();
        modelAndView.addObject("bugCounts", bugCounts);
    }

    private void populateBugBarChart(ModelAndView modelAndView){
        List<Map<String, Object>> bugData = bugService.getBugDataForStackedBarChart();
        modelAndView.addObject("bugData", bugData);
    }

}