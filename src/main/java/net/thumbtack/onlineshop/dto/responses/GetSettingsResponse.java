package net.thumbtack.onlineshop.dto.responses;

public class GetSettingsResponse {

    private int maxNameLength;
    private int minPasswordLength;

    public GetSettingsResponse(int maxNameLength, int minPasswordLength){
        this.maxNameLength = maxNameLength;
        this.minPasswordLength = minPasswordLength;
    }

}
