package bnifsc.util;

import java.io.IOException;
import java.net.URLEncoder;
import java.nio.ByteBuffer;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.logging.Logger;

import bnifsc.entites.Branch;

import com.google.appengine.tools.cloudstorage.GcsFileOptions;
import com.google.appengine.tools.cloudstorage.GcsFilename;
import com.google.appengine.tools.cloudstorage.GcsOutputChannel;
import com.google.appengine.tools.cloudstorage.GcsService;
import com.google.appengine.tools.cloudstorage.GcsServiceFactory;
import com.google.appengine.tools.cloudstorage.ListItem;
import com.google.appengine.tools.cloudstorage.ListOptions;
import com.google.appengine.tools.cloudstorage.ListResult;
import persist.BranchOfy;

public class SiteMap {
	public static final BranchOfy branchOfy = new BranchOfy();
	public final static Logger logger = Logger.getLogger(SiteMap.class
			.getName());
	public static GcsService gcsService = GcsServiceFactory.createGcsService();
	public static String WEBSITE_URL = "http://bnifsc.in/#!/";

	public List<String> createFile(String name, StringBuilder data) {
		List<String> list = new ArrayList<String>();
		try {
			GcsOutputChannel outputChannel = gcsService.createOrReplace(
					this.getGcsFile(name), GcsFileOptions.getDefaultInstance());
			String content = data.toString();
			outputChannel.write(ByteBuffer.wrap(content.getBytes()));
			list.add(name);
			outputChannel.close();
		} catch (IOException e) {
			logger.warning(e.getMessage());
		}
		return list;
	}

	public List<String> createSiteMapIndex() {
		StringBuilder strBuilder = new StringBuilder();
		List<String> siteMap = new ArrayList<String>();
		strBuilder
				.append("<?xml version=\"1.0\" encoding=\"UTF-8\"?><sitemapindex xmlns=\"http://www.sitemaps.org/schemas/sitemap/0.9\">");
		try {
			ListResult listResult = gcsService.list("seo-bnifsc",
					ListOptions.DEFAULT);
			while (listResult.hasNext()) {
				ListItem listItem = listResult.next();
				strBuilder.append(" <sitemap><loc>"
						+ "http://bnifsc.in/sitemap/"
						+ URLEncoder.encode(listItem.getName())
						+ "</loc></sitemap>");
			}
		} catch (IOException e) {
			logger.warning(e.getMessage());
		}
		strBuilder.append("</sitemapindex>");
		this.createFile("sitemapindex.xml", strBuilder);
		siteMap.add("siteMap created");
		return siteMap;
	}

	private GcsFilename getGcsFile(String name) {
		return new GcsFilename("seo-bnifsc", name);
	}

	public List<String> createSiteMap() {
		Branch branch = new Branch();
		List<String> response = new ArrayList<String>();
		List<Branch> banksName = branchOfy.banksList();
		Iterator<Branch> itr = banksName.iterator();
		int count = 1;
		while (itr.hasNext()) {
			StringBuilder siteMapXml = new StringBuilder();
			siteMapXml.append("<?xml version='1.0' encoding='UTF-8'?>");
			siteMapXml
					.append("<urlset xmlns='http://www.sitemaps.org/schemas/sitemap/0.9'>");
			String bankName = itr.next().getBankName();
			siteMapXml.append("<url>  <loc>" + SiteMap.WEBSITE_URL
					+ URLEncoder.encode(bankName) + "</loc></url>");
			branch.setBankName(bankName);
			List<Branch> states = branchOfy.statesList(bankName);
			Iterator<Branch> stateItr = states.iterator();
			while (stateItr.hasNext()) {
				String state = stateItr.next().getState();
				siteMapXml.append("<url>  <loc>" + SiteMap.WEBSITE_URL
						+ URLEncoder.encode(bankName + "/" + state)
						+ "</loc></url>");
				branch.setState(state);
				List<Branch> districts = branchOfy.districtsList(bankName,state);
				Iterator<Branch> districtItr = districts.iterator();
				while (districtItr.hasNext()) {
					String district = districtItr.next().getDistrict();
					siteMapXml.append("<url>  <loc>"
							+ SiteMap.WEBSITE_URL
							+ URLEncoder.encode(bankName + "/" + state + "/"
									+ district) + "</loc></url>");
					branch.setDistrict(district);
					List<Branch> branches = branchOfy.branches(bankName,state,district);
					Iterator<Branch> branchItr = branches
							.iterator();
					while (branchItr.hasNext()) {
						Branch b = branchItr.next();
						String branchString = b.getBranchName() + "/"
								+ b.getIfsc();
						siteMapXml.append("<url>  <loc>"
								+ SiteMap.WEBSITE_URL
								+ URLEncoder.encode(bankName + "/" + state
										+ "/" + district + "/" + branchString)
								+ "</loc></url>");
					}
				}
			}
			siteMapXml.append("</urlset>");
			this.createFile("sitemap" + count++ + ".xml", siteMapXml);
			response.add(bankName);
		}
		return response;
	}
}
