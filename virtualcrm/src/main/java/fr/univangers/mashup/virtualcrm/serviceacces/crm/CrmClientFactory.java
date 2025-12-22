package fr.univangers.mashup.virtualcrm.serviceacces.crm;

public class CrmClientFactory {
    private static final CrmClient crmClient = new CrmClientImpl();

    public static CrmClient getCrmClient() {
        return crmClient;
    }
}
