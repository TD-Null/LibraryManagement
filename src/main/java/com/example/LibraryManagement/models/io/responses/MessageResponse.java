package com.example.LibraryManagement.models.io.responses;

import lombok.AllArgsConstructor;
import lombok.Getter;

// Class that contains a message response as an output for API requests.
@Getter
@AllArgsConstructor
public class MessageResponse
{
    private String message;
}
