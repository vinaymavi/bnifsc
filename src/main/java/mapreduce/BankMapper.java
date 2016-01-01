// Copyright 2012 Google Inc. All Rights Reserved.

package mapreduce;

import entities.Bank;
import com.google.appengine.api.datastore.Entity;
import com.google.appengine.tools.mapreduce.MapOnlyMapper;

import java.util.Random;
import java.util.logging.Logger;

import static com.google.common.base.Preconditions.checkNotNull;

/**
 * Create Entities and push document to search index.
 */
public class BankMapper extends MapOnlyMapper<byte[], Entity> {

    private static final long serialVersionUID = 409204195454478863L;
    public static Logger logger = Logger.getLogger(BankMapper.class.getName());
    private final String kind;
    private final Random random = new Random();

    public BankMapper(String kind) {
        this.kind = checkNotNull(kind, "Null kind");
    }

    @Override
    public void map(byte[] csvLine) {
        String bankCSV = new String(csvLine);
        logger.warning(bankCSV);
        Bank bank = Bank.createFromCSV(bankCSV);
        Entity entity = new Entity(kind);
        entity.setProperty("name", bank.getName());
        entity.setProperty("addDate", bank.getAddDate());
        emit(entity);
    }
}
