package bnifsc.util;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.ObjectInputStream;
import java.nio.ByteBuffer;
import java.nio.channels.Channels;
import java.util.ArrayList;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;

import bnifsc.entites.Bank;
import bnifsc.entites.Branch;

import com.google.appengine.tools.cloudstorage.GcsFilename;
import com.google.appengine.tools.cloudstorage.GcsInputChannel;
import com.google.appengine.tools.cloudstorage.GcsService;
import com.google.appengine.tools.cloudstorage.GcsServiceFactory;
import com.google.appengine.tools.cloudstorage.RetryParams;
import com.google.gson.Gson;

@SuppressWarnings("serial")
public class BulkUpload {
	private final static Logger logger = Logger.getLogger(BulkUpload.class
			.getName());
	GcsService gcsService = GcsServiceFactory.createGcsService();
	private final Gson gson = new Gson();
	private String bucket;
	private String fileName;	

	public List<Bank> importBankNames() {
		GcsFilename filename = new GcsFilename(this.getBucket(),
				this.getFileName());
		GcsInputChannel readChannel = null;
		BufferedReader reader = null;
		try {
			readChannel = gcsService.openReadChannel(filename, 0);
			reader = new BufferedReader(Channels.newReader(readChannel, "UTF8"));
			String jsonData = "";
			String line = "";
			while ((line = reader.readLine()) != null) {
				jsonData += line;
			}

			Bank[] bankes = gson.fromJson(jsonData, Bank[].class);
			List<Bank> insertedBankes = new ArrayList<Bank>();
			for (Bank bank : bankes) {
				insertedBankes.add(bank.save());
			}

			return insertedBankes;
		} catch (IOException e) {
			logger.warning(e.getMessage());
		} finally {
			try {
				if (reader != null) {
					reader.close();
				}
			} catch (IOException e) {
				logger.warning(e.getMessage());
			}

		}

		return null;
	}

	public List<Branch> importBranch() {
		GcsFilename filename = new GcsFilename(this.getBucket(),
				this.getFileName());
		GcsInputChannel readChannel = null;
		BufferedReader reader = null;
		StringBuilder stringBuilder = new StringBuilder();
		String bankName="";
		try {
			readChannel = gcsService.openReadChannel(filename, 0);
			reader = new BufferedReader(Channels.newReader(readChannel, "UTF8"));
			String jsonData = "";
			String line = "";
			while ((line = reader.readLine()) != null) {
				stringBuilder.append(line);
			}
			
			jsonData = stringBuilder.toString();					
			Branch[] branches = gson.fromJson(jsonData, Branch[].class);
			List<Branch> insertedBankes = new ArrayList<Branch>();
			for (Branch branch : branches) {
				bankName=branch.getName();
				branch.setName(branch.getName().trim());
				branch.setState(branch.getState().trim());
				branch.setDistrict(branch.getDistrict().trim());
				branch.setAddress(branch.getAddress());
				branch.setPhone(branch.getPhone().trim());
				branch.setPincode(branch.getPincode().trim());
				branch.setIfsc(branch.getIfsc().trim());
				branch.setSwift(branch.getSwift().trim());
				branch.setBranchName(branch.getBranchName().trim());
				insertedBankes.add(branch.save());
			}
			logger.warning(bankName+" count ="+insertedBankes.size());
			return insertedBankes;
		} catch (IOException e) {
			logger.warning(e.getMessage());
		} finally {
			try {
				if (reader != null) {
					reader.close();
				}
			} catch (IOException e) {
				logger.warning(e.getMessage());
			}

		}

		return null;
	}

	public String getBucket() {
		return bucket;
	}

	public void setBucket(String bucket) {
		this.bucket = bucket;
	}

	public String getFileName() {
		return fileName;
	}

	public void setFileName(String fileName) {
		this.fileName = fileName;
	}
}
