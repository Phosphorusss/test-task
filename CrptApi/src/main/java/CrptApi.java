import com.fasterxml.jackson.databind.ObjectMapper;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;

import java.io.IOException;
import java.util.Arrays;
import java.util.List;
import java.util.concurrent.Semaphore;

public class CrptApi {
    private Semaphore semaphore;
    private Integer requestLimit;
    private CloseableHttpClient httpClient;

    public CrptApi(Integer requestLimit) {
        this.requestLimit = requestLimit;
        this.semaphore = new Semaphore(requestLimit);
        this.httpClient = HttpClients.createDefault();
    }
    public void createDocument(Document document, String signature) throws IOException {
        if (!semaphore.tryAcquire()) {
            System.out.println("limit on the number of requests to API");
            return;
        }

        ObjectMapper objectMapper = new ObjectMapper();
        String json = objectMapper.writeValueAsString(document);
        System.out.println(json);

        String url = "https://ismp.crpt.ru/api/v3/lk/documents/create";
        HttpPost httpPost = new HttpPost(url);

        httpPost.setHeader("Authorization", signature);
        httpPost.setHeader("Content-type", "application/json");

        StringEntity entity = new StringEntity(json);
        httpPost.setEntity(entity);

        CloseableHttpResponse response = httpClient.execute(httpPost);
        int statusCode = response.getStatusLine().getStatusCode();
        System.out.println(statusCode);

        semaphore.release();
    }

    public static void main(String[] args) throws IOException {
        Values values = new Values("string", "2020-01-23", "12", "string", "26549651", "2022-02-03", "string", "03", "10");
        Document document = new Document("string", "1", "string", "string", true, "5216961", "564646132", "564646132", "2023-08-26", "string", Arrays.asList(values), "string", "string");

        CrptApi obj = new CrptApi(5);
        obj.createDocument(document, "signature");
    }

}

final class Document {
    private String description;
    private String doc_id;
    private String doc_status;
    private String doc_type;
    private boolean importRequest;
    private String owner_inn;
    private String participant_inn;
    private String producer_inn;
    private String production_date;
    private String production_type;
    private List products;
    private String reg_date;
    private String reg_number;

    public Document(String description, String doc_id, String doc_status, String doc_type, boolean importRequest, String owner_inn, String participant_inn,
                    String producer_inn, String production_date, String production_type, List products, String reg_date, String reg_number) {
        this.description = description;
        this.doc_id = doc_id;
        this.doc_status = doc_status;
        this.doc_type = doc_type;
        this.importRequest = importRequest;
        this.owner_inn = owner_inn;
        this.participant_inn = participant_inn;
        this.producer_inn = producer_inn;
        this.production_date = production_date;
        this.production_type = production_type;
        this.products = products;
        this.reg_date = reg_date;
        this.reg_number = reg_number;
    }

    public String getDescription() {
        return description;
    }

    public String getDoc_id() {
        return doc_id;
    }

    public String getDoc_status() {
        return doc_status;
    }

    public String getDoc_type() {
        return doc_type;
    }

    public boolean isImportRequest() {
        return importRequest;
    }

    public String getOwner_inn() {
        return owner_inn;
    }

    public String getParticipant_inn() {
        return participant_inn;
    }

    public String getProducer_inn() {
        return producer_inn;
    }

    public String getProduction_date() {
        return production_date;
    }
    public String getProduction_type() {
        return production_type;
    }

    public List getProducts() {
        return products;
    }

    public String getReg_date() {
        return reg_date;
    }

    public String getReg_number() {
        return reg_number;
    }
}

final class Values {
    private String certificate_document;
    private String certificate_document_date;
    private String certificate_document_number;
    private String owner_inn;
    private String producer_inn;
    private String production_date;
    private String tnved_code;
    private String uit_code;
    private String uitu_code;

    public Values(String certificate_document, String certificate_document_date, String certificate_document_number, String owner_inn,
                  String producer_inn, String production_date, String tnved_code, String uit_code, String uitu_code) {
        this.certificate_document = certificate_document;
        this.certificate_document_date = certificate_document_date;
        this.certificate_document_number = certificate_document_number;
        this.owner_inn = owner_inn;
        this.producer_inn = producer_inn;
        this.production_date = production_date;
        this.tnved_code = tnved_code;
        this.uit_code = uit_code;
        this.uitu_code = uitu_code;
    }

    public String getCertificate_document() {
        return certificate_document;
    }

    public String getCertificate_document_date() {
        return certificate_document_date;
    }

    public String getCertificate_document_number() {
        return certificate_document_number;
    }

    public String getOwner_inn() {
        return owner_inn;
    }

    public String getProducer_inn() {
        return producer_inn;
    }

    public String getProduction_date() {
        return production_date;
    }

    public String getTnved_code() {
        return tnved_code;
    }

    public String getUit_code() {
        return uit_code;
    }

    public String getUitu_code() {
        return uitu_code;
    }
}
