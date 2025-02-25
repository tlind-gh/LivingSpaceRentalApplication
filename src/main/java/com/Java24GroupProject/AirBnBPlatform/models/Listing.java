package com.Java24GroupProject.AirBnBPlatform.models;


import com.Java24GroupProject.AirBnBPlatform.models.supportClasses.DateRange;
import com.Java24GroupProject.AirBnBPlatform.models.supportClasses.ListingUtilities;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.DBRef;
import org.springframework.data.mongodb.core.mapping.Document;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Set;

@Document(collection = "listings")
public class Listing {
    @Id
    private String id;

    private String title;

    private String description;

    @NotNull(message = "A price per night is required.")
    @Positive(message = "Price per night must be greater than zero")
    private BigDecimal price_per_night;

    @NotNull(message = "Capacity limit must be set.")
    @Positive(message = "Capacity must be greater than zero")
    private Integer capacity;

    private Set<ListingUtilities> utilities;

    @NotNull(message = "Listing must have at least one host.")
    @DBRef
    private User host;

    private List<String> image_urls;

    // location by city so one can search by city later on
    private String location;

    private List<DateRange> availableDates;

    @CreatedDate
    private LocalDateTime createdAt;

    private LocalDateTime updatedAt;

    public Listing() {
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public Integer getCapacity() {
        return capacity;
    }

    public void setCapacity(Integer capacity) {
        this.capacity = capacity;
    }

    public User getHost() {
        return host;
    }

    public void setHost(User hosts) {
        this.host = hosts;
    }

    public Set<ListingUtilities> getUtilities() {
        return utilities;
    }

    public void setUtilities(Set<ListingUtilities> utilities) {
        this.utilities = utilities;
    }

    public List<String> getImage_urls() {
        return image_urls;
    }

    public void setImage_urls(List<String> image_urls) {
        this.image_urls = image_urls;
    }

    public String getLocation() {
        return location;
    }

    public void setLocation(String location) {
        this.location = location;
    }

    public LocalDateTime getCreatedAt() {
        return createdAt;
    }

    public void setCreatedAt(LocalDateTime createdAt) {
        this.createdAt = createdAt;
    }

    public BigDecimal getPrice_per_night() {
        return price_per_night;
    }

    public void setPrice_per_night(BigDecimal price_per_night) {
        this.price_per_night = price_per_night;
    }

    public List<DateRange> getAvailableDates() {
        return availableDates;
    }

    public void setAvailableDates(List<DateRange> availableDates) {
        this.availableDates = availableDates;
    }


    public LocalDateTime getUpdatedAt() {
        return updatedAt;
    }

    public void setUpdatedAt(LocalDateTime updatedAt) {
        this.updatedAt = updatedAt;
    }

    public void addAvailableDateRange(DateRange dateRange) {
        for (DateRange availableDateRange : availableDates) {
            //check if there is overlap with existing dates
            if (dateRange.hasOverlapWithAnotherDateRange(availableDateRange)) {
                throw new IllegalArgumentException("dates could not be added, as they overlap with existing available date ranges");

                //if dates are identical to dates in list throw error
            } else if (dateRange.isIdenticalToAnotherDateRange(availableDateRange)) {
                throw new IllegalArgumentException("dates could not be added, already in available dates for listing");
            }
        }

        //add dates
        availableDates.add(dateRange);

        //run through list and fuse any dateRanges that are adjacent
        boolean continueFuseDateRanges = true;
        while (continueFuseDateRanges) {
            continueFuseDateRanges = fuseAdjacentDateRanges();
        }
    }

    public boolean fuseAdjacentDateRanges() {
        boolean dateRangeFused = false;
        for (DateRange availableDates1 : availableDates) {
            for (DateRange availableDates2 : availableDates) {
                if (availableDates1.isIdenticalToAnotherDateRange(availableDates2)) {
                    continue;
                }
                if (availableDates1.getStartDate().isEqual(availableDates2.getEndDate())) {
                    availableDates1.setStartDate(availableDates2.getStartDate());
                    availableDates.remove(availableDates2);
                    dateRangeFused = true;
                    break;
                } else if (availableDates2.getStartDate().isEqual(availableDates1.getEndDate()) || availableDates2.getStartDate().minusDays(1).isEqual(availableDates1.getEndDate())) {
                    availableDates1.setEndDate(availableDates2.getEndDate());
                    availableDates.remove(availableDates2);
                    dateRangeFused = true;
                    break;
                }
            }
            if (dateRangeFused) {
                break;
            }

        }
        return dateRangeFused;
    }
}