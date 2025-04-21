package lk.ijse.gdse.serenitymentalhealththerapycenter.dto.tm;

import javafx.beans.property.SimpleStringProperty;
import javafx.beans.property.StringProperty;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.Set;

public class TimeSlotRow {

    private final SimpleStringProperty time;
    private final List<AvailabilityEntry> availabilityList;
    private String availabilityId;  // Store the availability ID here

    public TimeSlotRow(String time, Set<LocalDate> dates) {
        this.time = new SimpleStringProperty(time);
        this.availabilityList = new ArrayList<>();
        for (LocalDate date : dates) {
            availabilityList.add(new AvailabilityEntry(date, new SimpleStringProperty("")));
        }
    }

    // Property for time
    public StringProperty timeProperty() {
        return time;
    }

    // Get the availability status for a specific date
    public StringProperty availabilityProperty(LocalDate date) {
        for (AvailabilityEntry entry : availabilityList) {
            if (entry.getDate().equals(date)) {
                return entry.getAvailability();
            }
        }
        return null; // Or throw an exception if you prefer
    }

    // Set availability value for a specific date
    public void setAvailability(LocalDate date, String value) {
        StringProperty property = availabilityProperty(date);
        if (property != null) {
            property.set(value);
        }
    }

    // Helper class to replace the Map entry
    private static class AvailabilityEntry {
        private final LocalDate date;
        private final StringProperty availability;

        public AvailabilityEntry(LocalDate date, StringProperty availability) {
            this.date = date;
            this.availability = availability;
        }

        public LocalDate getDate() {
            return date;
        }

        public StringProperty getAvailability() {
            return availability;
        }
    }
}
