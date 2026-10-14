package com.placement.spms.service;

import com.placement.spms.dto.OptionsDtos.AppOptions;
import com.placement.spms.dto.OptionsDtos.OptionItem;
import com.placement.spms.entity.*;
import org.springframework.stereotype.Service;

import java.time.Year;
import java.util.Arrays;
import java.util.List;
import java.util.stream.IntStream;

@Service
public class OptionsService {
    public AppOptions options() {
        int currentYear = Year.now().getValue();
        return new AppOptions(
                enumOptions(Role.values(), List.of(Role.ADMIN.name())),
                enumOptions(ApplicationStatus.values(), List.of()),
                enumOptions(DriveStatus.values(), List.of()),
                enumOptions(InterviewMode.values(), List.of()),
                enumOptions(AccountStatus.values(), List.of()),
                List.of(
                        item("Full-time", "Full-time", "Permanent campus placement"),
                        item("Internship", "Internship", "Short-term role for students"),
                        item("Internship-to-full-time", "Internship-to-full-time", "Internship with possible conversion"),
                        item("Part-time", "Part-time", "Limited weekly work schedule"),
                        item("Contract", "Contract", "Fixed-duration opportunity")
                ),
                List.of(
                        item("Computer Science", "Computer Science", "CSE and related programs"),
                        item("Information Technology", "Information Technology", "IT and software programs"),
                        item("Electronics", "Electronics", "ECE and electronics programs"),
                        item("Electrical", "Electrical", "Electrical engineering"),
                        item("Mechanical", "Mechanical", "Mechanical engineering"),
                        item("Civil", "Civil", "Civil engineering"),
                        item("Business Administration", "Business Administration", "Management programs"),
                        item("All", "All departments", "Open to every department")
                ),
                List.of(
                        item("B.Tech", "B.Tech", "Bachelor of Technology"),
                        item("B.E.", "B.E.", "Bachelor of Engineering"),
                        item("B.Sc", "B.Sc", "Bachelor of Science"),
                        item("BCA", "BCA", "Bachelor of Computer Applications"),
                        item("M.Tech", "M.Tech", "Master of Technology"),
                        item("MCA", "MCA", "Master of Computer Applications"),
                        item("MBA", "MBA", "Master of Business Administration")
                ),
                IntStream.rangeClosed(currentYear - 1, currentYear + 6)
                        .mapToObj(year -> item(String.valueOf(year), String.valueOf(year), year == currentYear ? "Current graduating batch" : "Graduation batch"))
                        .toList(),
                IntStream.rangeClosed(0, 10)
                        .mapToObj(count -> item(String.valueOf(count), count == 0 ? "No active backlogs" : count + " active backlog" + (count == 1 ? "" : "s"), "Maximum active backlog count"))
                        .toList()
        );
    }

    private List<OptionItem> enumOptions(Enum<?>[] values, List<String> disabledValues) {
        return Arrays.stream(values)
                .map(value -> new OptionItem(value.name(), label(value.name()), null, disabledValues.contains(value.name())))
                .toList();
    }

    private OptionItem item(String value, String label, String description) {
        return new OptionItem(value, label, description, false);
    }

    private String label(String value) {
        String lower = value.replace('_', ' ').toLowerCase();
        return Character.toUpperCase(lower.charAt(0)) + lower.substring(1);
    }
}
