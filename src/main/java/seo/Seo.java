package seo;

import java.net.URLDecoder;
import java.util.logging.Logger;

import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import entities.Branch;
import persist.BranchOfy;

public class Seo extends HttpServlet {
    public static final Logger logger = Logger.getLogger(Seo.class.getName());
    public static final BranchOfy branchOfy = new BranchOfy();
    private static final long serialVersionUID = 1L;

    @Override
    public void doGet(HttpServletRequest req, HttpServletResponse resp) {
        String queryString = req.getParameter("_escaped_fragment_");
        if (queryString.indexOf('/') != 0 && queryString.length() > 0) {
            queryString = "/" + queryString;
        } else if (queryString.indexOf('/') == 0 && queryString.length() == 1) {
            queryString = "";
        }
        Branch branch = new Branch();
        String[] queryParams = URLDecoder.decode(queryString).split("/");
        logger.warning(URLDecoder.decode(queryString));
        logger.warning("" + queryParams.length);
        switch (queryParams.length) {
            case 1:
                req.setAttribute("banksList", branchOfy.banksList());
                try {
                    req.getRequestDispatcher("/seo/banks.jsp").forward(req, resp);
                } catch (Exception e) {
                    logger.warning(e.getMessage());
                }

                break;
            case 2:
                branch.setBankName(queryParams[1]);
                req.setAttribute("bank", queryParams[1]);
                req.setAttribute("states", branchOfy.statesList(branch.getBankName()));
                try {
                    req.getRequestDispatcher("/seo/states.jsp").forward(req, resp);
                } catch (Exception e) {
                    logger.warning(e.getMessage());
                }
                break;
            case 3:
                branch.setBankName(queryParams[1]);
                branch.setState(queryParams[2]);
                req.setAttribute("bank", queryParams[1]);
                req.setAttribute("state", queryParams[2]);
                req.setAttribute("districts", branchOfy.districtsList(branch.getBankName(), branch.getState()));
                try {
                    req.getRequestDispatcher("/seo/districts.jsp").forward(req,
                            resp);
                } catch (Exception e) {
                    logger.warning(e.getMessage());
                }
                break;
            case 4:
                branch.setBankName(queryParams[1]);
                branch.setState(queryParams[2]);
                branch.setDistrict(queryParams[3]);
                req.setAttribute("bank", queryParams[1]);
                req.setAttribute("state", queryParams[2]);
                req.setAttribute("district", queryParams[3]);
                req.setAttribute("branches", branchOfy.branches(branch.getBankName(), branch.getState(), branch.getDistrict()));
                try {
                    req.getRequestDispatcher("/seo/branches.jsp")
                            .forward(req, resp);
                } catch (Exception e) {
                    logger.warning(e.getMessage());
                }
                break;
            case 6:
                branch.setBankName(queryParams[1]);
                branch.setState(queryParams[2]);
                branch.setDistrict(queryParams[3]);
                branch.setIfsc(queryParams[5]);
                req.setAttribute("bank", queryParams[1]);
                req.setAttribute("state", queryParams[2]);
                req.setAttribute("district", queryParams[3]);
                req.setAttribute("branchName", queryParams[4]);
                logger.warning("key=" + queryParams[5]);
                req.setAttribute("branch", branchOfy.loadByIFSC(branch.getIfsc()));
                try {
                    req.getRequestDispatcher("/seo/branch.jsp").forward(req, resp);
                } catch (Exception e) {
                    logger.warning(e.getMessage());
                }
                break;
            default:
                break;
        }

    }

}
