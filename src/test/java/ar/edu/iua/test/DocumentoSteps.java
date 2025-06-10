package ar.edu.iua.test;


import org.junit.jupiter.api.Assertions;

import io.cucumber.java.en.And;
import io.cucumber.java.en.Given;
import io.cucumber.java.en.Then;
import io.cucumber.java.en.When;

import java.util.HashMap;
import java.util.Map;
import java.util.Set;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.junit.jupiter.api.Assertions.assertFalse;

public class DocumentoSteps {
    private String rolUsuario;
    private String rolDestinatario;
    private String ministerioUsuario;
    private String ministerioDestinatario;

    @Given("el usuario tiene el rol de {string}")
    public void el_usuario_tiene_el_rol_de(String rol) {
        this.rolUsuario = rol;
    }

    @And("el usuario pertenece al ministerio {string}")
    public void el_usuario_pertenece_al_ministerio(String ministerio) {
        this.ministerioUsuario = ministerio;
    }

    @When("selecciona como destinatario a {string}")
    public void selecciona_como_destinatario_a(String rolDest) {
        this.rolDestinatario = rolDest;
    }

    @And("el destinatario pertenece al ministerio {string}")
    public void el_destinatario_pertenece_al_ministerio(String ministerio) {
        this.ministerioDestinatario = ministerio;
    }

    @Then("el sistema permite el envío del documento")
    public void elSistemaPermiteElEnvioDelDocumento() {
        assertTrue(tienePermiso(), "El rol '" + rolUsuario + "' no tiene permiso para enviar a '" + rolDestinatario + "'");
    }

    @Then("el sistema no permite el envío del documento")
    public void elSistemaNoPermiteElEnvioDelDocumento() {
        assertFalse(tienePermiso(), "El rol '" + rolUsuario + "' no debería tener permiso para enviar a '" + rolDestinatario + "'");
    }

    private boolean tienePermiso() {
        if ("Mesa de Control".equals(rolUsuario)) return true;
        if ("Presidente".equals(rolUsuario)) return "Ministro".equals(rolDestinatario);
        if ("Vicepresidente".equals(rolUsuario)) return "Ministro".equals(rolDestinatario);
        if ("Ministro".equals(rolUsuario)) {
            return "Presidente".equals(rolDestinatario) || "Vicepresidente".equals(rolDestinatario)
                    || (mismoMinisterio() && ("Asesor".equals(rolDestinatario) || "Funcionarios".equals(rolDestinatario)));
        }
        if ("Asesor".equals(rolUsuario)) {
            return mismoMinisterio() && ("Ministro".equals(rolDestinatario) || "Funcionarios".equals(rolDestinatario));
        }
        if ("Funcionarios".equals(rolUsuario)) {
            return mismoMinisterio() && "Asesor".equals(rolDestinatario);
        }
        return false;
    }

    private boolean mismoMinisterio() {
        return ministerioUsuario != null && ministerioUsuario.equals(ministerioDestinatario);
    }
}