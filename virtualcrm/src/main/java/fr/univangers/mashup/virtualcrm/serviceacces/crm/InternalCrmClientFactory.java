package fr.univangers.mashup.virtualcrm.serviceacces.crm;

public class InternalCrmClientFactory {
    private static final InternalCrmClient crmClient = new InternalCrmThriftClient();

    public static InternalCrmClient getInternalCrmClient() {
        return crmClient;
    }
}
