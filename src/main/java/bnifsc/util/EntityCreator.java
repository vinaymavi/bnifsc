// Copyright 2012 Google Inc. All Rights Reserved.

package bnifsc.util;

import bnifsc.entites.Branch;
import com.google.appengine.api.datastore.Entity;
import com.google.appengine.tools.mapreduce.MapOnlyMapper;
import org.apache.commons.lang3.StringUtils;
import org.apache.commons.lang3.text.WordUtils;

import java.util.Date;
import java.util.List;
import java.util.Random;
import java.util.logging.Logger;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import persist.BranchOfy;

import static com.google.common.base.Preconditions.checkNotNull;

/**
 * Creates random entities.
 *
 * @author ohler@google.com (Christian Ohler)
 */
class EntityCreator extends MapOnlyMapper<byte[], Entity> {

    private static final long serialVersionUID = 409204195454478863L;
    public static Logger logger = Logger.getLogger(EntityCreator.class.getName());
    private final String kind;
    private final Random random = new Random();

    public EntityCreator(String kind) {
        this.kind = checkNotNull(kind, "Null kind");
    }

    @Override
    public void map(byte[] value) {
        logger.warning(new String(value));
        BranchOfy bf = new BranchOfy();
        String branch = new String(value);
        String[] branchPros = branch.split("\",\"");
//        Getting props from branch.
        String bankName = StringUtils.substringAfter(WordUtils.capitalizeFully(branchPros[0]).trim(), "\"");
        String state = StringUtils.substringBefore(WordUtils.capitalizeFully(branchPros[8]).trim(), "\"");
        String district = WordUtils.capitalizeFully(branchPros[7]).trim();
        String city = WordUtils.capitalizeFully(branchPros[6]).trim();
        String branchName = WordUtils.capitalizeFully(branchPros[3]).trim();
        String custCare;
        String email;
        String mobile;
        String phone = branchPros[5].split("\\.")[0].trim();
        String address = WordUtils.capitalizeFully(branchPros[4]).trim();

        String ifsc = branchPros[1].trim();
        String branchCode = ifsc.substring(ifsc.length() - 6);
        String micr = branchPros[2].split("\\.")[0].trim();
        String swift;
        Pattern pinCodePattern = Pattern.compile("([0-9]{6})");
        Matcher matcher = pinCodePattern.matcher(address);
        String pinCode = "";
        if (matcher.find()) {
            pinCode = matcher.group();
        }

//        Add and update date.
        Date updateDate;
        Date addDate;
        List<Branch> branchList = bf.loadByIFSC(ifsc);
        if (branchList.size() > 0) {
            Branch b = branchList.get(0);
            updateDate = new Date();
            addDate = b.getAddDate();
        } else {
            updateDate = null;
            addDate = new Date();
        }


//        Entity Creator
        Entity entity = new Entity(kind, ifsc);
        entity.setProperty("bankName", bankName);
        entity.setProperty("state", state);
        entity.setProperty("district", district);
        entity.setProperty("city", city);
        entity.setProperty("branchName", branchName);
        entity.setProperty("branchCode", branchCode);
        entity.setProperty("phone", phone);
        entity.setProperty("address", address);
        entity.setProperty("ifsc", ifsc);
        entity.setProperty("micr", micr);
        entity.setProperty("pinCode", pinCode);
        entity.setProperty("addDate", addDate);
        entity.setProperty("updateDate", updateDate);
        emit(entity);
    }
}
