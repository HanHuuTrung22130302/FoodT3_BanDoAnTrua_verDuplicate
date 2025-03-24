package hcmuaf.nlu.edu.vn.testproject.services;

import hcmuaf.nlu.edu.vn.testproject.daos.AccdetailDAO;
import hcmuaf.nlu.edu.vn.testproject.models.AccountDetail;

import java.util.List;

public class AccdetailService {
    public List<AccountDetail> getAccDetails() {
        AccdetailDAO accDetailDAO = new AccdetailDAO();
        List<AccountDetail> listAcc = accDetailDAO.getAllAccDetail();
        return listAcc;
    }
}
