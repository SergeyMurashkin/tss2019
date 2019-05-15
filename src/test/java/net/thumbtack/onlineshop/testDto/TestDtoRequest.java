package net.thumbtack.onlineshop.testDto;

import net.thumbtack.onlineshop.dto.requests.AddCategoryRequest;
import org.junit.Test;

import javax.validation.Valid;

public class TestDtoRequest {

    @Test
    @Valid
    public void testAddCategoryRequest() {
        AddCategoryRequest request = new AddCategoryRequest();
        request.setName("одежда");
        request.setParentId(100);
        checkRequest(request);
    }

    @Test
    @Valid
    public void testAddCategoryRequestWithBigName() {
        @Valid AddCategoryRequest request = new AddCategoryRequest("одеждаодеждаодеждаодеждаодеждаодеждаодеждаодеждаодежда", -100);
        checkRequest(request);
    }


    private void checkRequest(@Valid AddCategoryRequest request) {
        System.out.println(request);
    }


}
