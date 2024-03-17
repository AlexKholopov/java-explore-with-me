package ru.practicum.explore.model.request;

import lombok.Data;
import ru.practicum.explore.model.request.RequestStatus;

import java.util.List;

@Data
public class ConfirmeRequestsInput {
    private List<Long> requestIds;
    private RequestStatus status;
}
