package hcmuaf.nlu.edu.vn.testproject.services;

import hcmuaf.nlu.edu.vn.testproject.daos.BannerDao;
import hcmuaf.nlu.edu.vn.testproject.models.Banner;

import java.util.List;

public class BannerService {
    BannerDao dao = new BannerDao();

    public List<Banner> getBanners() {
        List<Banner> banners = dao.getBanners();
        return banners;
    }

    public boolean addBanner(Banner banner) {
        dao.addBanner(banner);
        return false;
    }

    public boolean deleteBanner(int id) {
        dao.deleteBaner(id);
        return false;
    }

    public static void main(String[] args) {
        BannerService bannerService = new BannerService();
        List<Banner> banners = bannerService.getBanners();
        for (Banner banner : banners) {
            System.out.println(banner);
        }
    }
}
