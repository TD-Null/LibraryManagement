package com.example.LibraryManagement.models.io.requests.account_requests.librarian_requests;

import com.example.LibraryManagement.models.io.requests.account_requests.BarcodeValidationRequest;
import lombok.Getter;

import javax.validation.constraints.NotBlank;

@Getter
public class AddLibraryRackRequest extends BarcodeValidationRequest
{
    @NotBlank
    private String libraryName;

    private int number;

    @NotBlank
    private String locationIdentifier;

    public AddLibraryRackRequest(String barcode, String libraryName, int number, String locationIdentifier)
    {
        super(barcode);
        this.libraryName = libraryName;
        this.number = number;
        this.locationIdentifier = locationIdentifier;
    }
}
