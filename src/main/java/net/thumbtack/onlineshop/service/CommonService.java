package net.thumbtack.onlineshop.service;

import net.thumbtack.onlineshop.OnlineShopServer;
import net.thumbtack.onlineshop.dao.CommonDao;
import net.thumbtack.onlineshop.daoImpl.CommonDaoImpl;
import net.thumbtack.onlineshop.dto.responses.GetSettingsResponse;

public class CommonService {

    private CommonDao commonDao = new CommonDaoImpl();

    public GetSettingsResponse getServerSettings() {
        return new GetSettingsResponse(OnlineShopServer.MAX_NAME_LENGTH, OnlineShopServer.MIN_PASSWORD_LENGTH);
    }

    public void clearDataBase() {
        commonDao.clearDataBase();
    }

}
