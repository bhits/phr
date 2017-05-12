package gov.samhsa.c2s.phr.domain;

public enum UploadedDocumentContentType {
    TEXT_XML ("text/xml");

    private final String name;

    UploadedDocumentContentType(String s) {
        name = s;
    }

    public boolean equalsName(String compareTypeName){
        return name.equals(compareTypeName);
    }

    public String toString(){
        return this.name;
    }
}
