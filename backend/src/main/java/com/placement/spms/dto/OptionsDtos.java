package com.placement.spms.dto;

import java.util.List;

public class OptionsDtos {
    public record OptionItem(String value, String label, String description, boolean disabled) {}

    public record AppOptions(
            List<OptionItem> roles,
            List<OptionItem> applicationStatuses,
            List<OptionItem> driveStatuses,
            List<OptionItem> interviewModes,
            List<OptionItem> accountStatuses,
            List<OptionItem> employmentTypes,
            List<OptionItem> departments,
            List<OptionItem> degrees,
            List<OptionItem> graduationYears,
            List<OptionItem> backlogs
    ) {}
}
