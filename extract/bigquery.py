from google.cloud import bigquery

# Instantiates a client
bigquery_client = bigquery.Client()

class BigQuery(object):
    def __init__(self, *args, **kwargs):
        pass

    def bank_list(self):
        query = ('SELECT * FROM `bnifsc-beta.BNIFSC_IFSC_CODE.Dataprep_Bank_List` limit 10')
        query_job = bigquery_client.query(
            query,
            # Location must match that of the dataset(s) referenced in the query.
            location='US')  # API request - starts the query
        return query_job

    def state_list(self):
        query = ('SELECT * FROM `bnifsc-beta.BNIFSC_IFSC_CODE.Dataprep_State_List` limit 10')
        query_job = bigquery_client.query(
            query,
            # Location must match that of the dataset(s) referenced in the query.
            location='US')  # API request - starts the query
        return query_job

    def district_list(self):
        query = ('SELECT * FROM `bnifsc-beta.BNIFSC_IFSC_CODE.Dataprep_District_List_With_State` limit 10')
        query_job = bigquery_client.query(
            query,
            # Location must match that of the dataset(s) referenced in the query.
            location='US')  # API request - starts the query
        return query_job

    def city_list(self):
        query = ('SELECT * FROM `bnifsc-beta.BNIFSC_IFSC_CODE.Dataprep_City_List_With_State_District` limit 10')
        query_job = bigquery_client.query(
            query,
            # Location must match that of the dataset(s) referenced in the query.
            location='US')  # API request - starts the query
        return query_job

    def branch_list(self):
        query = ('SELECT * FROM `bnifsc-beta.BNIFSC_IFSC_CODE.Dataprep_Branch_List` limit 10')
        query_job = bigquery_client.query(
            query,
            # Location must match that of the dataset(s) referenced in the query.
            location='US')  # API request - starts the query
        return query_job
