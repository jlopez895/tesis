package ar.edu.iua.model;

public class RolPrincipalHolder {
	
	private static RolPrincipalHolder instance;
    private Integer idRolPrincipal;

    private RolPrincipalHolder() {
    }

    public static RolPrincipalHolder getInstance() {
        if (instance == null) {
            instance = new RolPrincipalHolder();
        }
        return instance;
    }

    public Integer getIdRolPrincipal() {
        return idRolPrincipal;
    }

    public void setIdRolPrincipal(Integer idRolPrincipal) {
        this.idRolPrincipal = idRolPrincipal;
    }

}
