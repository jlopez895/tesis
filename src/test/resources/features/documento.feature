Feature: Envío de documentos entre roles

  # Escenarios permitidos
  Scenario: Presidente puede enviar a Ministro
    Given el usuario tiene el rol de "Presidente"
    When selecciona como destinatario a "Ministro"
    Then el sistema permite el envío del documento

  Scenario: Vicepresidente puede enviar a Ministro
    Given el usuario tiene el rol de "Vicepresidente"
    When selecciona como destinatario a "Ministro"
    Then el sistema permite el envío del documento

  Scenario: Ministro puede enviar a Presidente
    Given el usuario tiene el rol de "Ministro"
    When selecciona como destinatario a "Presidente"
    Then el sistema permite el envío del documento

  Scenario: Ministro puede enviar a Vicepresidente
    Given el usuario tiene el rol de "Ministro"
    When selecciona como destinatario a "Vicepresidente"
    Then el sistema permite el envío del documento

  Scenario: Ministro puede enviar a Asesor del mismo ministerio
    Given el usuario tiene el rol de "Ministro"
    And el usuario pertenece al ministerio "MINISTERIO DE SALUD"
    When selecciona como destinatario a "Asesor"
    And el destinatario pertenece al ministerio "MINISTERIO DE SALUD"
    Then el sistema permite el envío del documento

  Scenario: Ministro puede enviar a Funcionarios del mismo ministerio
    Given el usuario tiene el rol de "Ministro"
    And el usuario pertenece al ministerio "MINISTERIO DE SALUD"
    When selecciona como destinatario a "Funcionarios"
    And el destinatario pertenece al ministerio "MINISTERIO DE SALUD"
    Then el sistema permite el envío del documento

  Scenario: Asesor puede enviar a Ministro del mismo ministerio
    Given el usuario tiene el rol de "Asesor"
    And el usuario pertenece al ministerio "MINISTERIO DE SALUD"
    When selecciona como destinatario a "Ministro"
    And el destinatario pertenece al ministerio "MINISTERIO DE SALUD"
    Then el sistema permite el envío del documento

  Scenario: Asesor puede enviar a Funcionarios del mismo ministerio
    Given el usuario tiene el rol de "Asesor"
    And el usuario pertenece al ministerio "MINISTERIO DE SALUD"
    When selecciona como destinatario a "Funcionarios"
    And el destinatario pertenece al ministerio "MINISTERIO DE SALUD"
    Then el sistema permite el envío del documento

  Scenario: Funcionarios puede enviar a Asesor del mismo ministerio
    Given el usuario tiene el rol de "Funcionarios"
    And el usuario pertenece al ministerio "MINISTERIO DE SALUD"
    When selecciona como destinatario a "Asesor"
    And el destinatario pertenece al ministerio "MINISTERIO DE SALUD"
    Then el sistema permite el envío del documento

  Scenario: Mesa de Control puede enviar a cualquier rol
    Given el usuario tiene el rol de "Mesa de Control"
    When selecciona como destinatario a "Presidente"
    Then el sistema permite el envío del documento

  Scenario: Mesa de Control puede enviar a cualquier rol
    Given el usuario tiene el rol de "Mesa de Control"
    When selecciona como destinatario a "Prensa"
    Then el sistema permite el envío del documento

  # Escenarios no permitidos
  Scenario: Asesor intenta enviar a Ministro de otro ministerio
    Given el usuario tiene el rol de "Asesor"
    And el usuario pertenece al ministerio "MINISTERIO DE DEFENSA"
    When selecciona como destinatario a "Ministro"
    And el destinatario pertenece al ministerio "MINISTERIO DE SALUD"
    Then el sistema no permite el envío del documento

  Scenario: Funcionarios intenta enviar a Asesor de otro ministerio
    Given el usuario tiene el rol de "Funcionarios"
    And el usuario pertenece al ministerio "MINISTERIO DE DEFENSA"
    When selecciona como destinatario a "Asesor"
    And el destinatario pertenece al ministerio "MINISTERIO DE SALUD"
    Then el sistema no permite el envío del documento
    
# Escenarios de envío NO permitido

Scenario: Asesor intenta enviar a Ministro de otro ministerio
  Given el usuario tiene el rol de "Asesor"
  And el usuario pertenece al ministerio "MINISTERIO DE DEFENSA"
  When selecciona como destinatario a "Ministro"
  And el destinatario pertenece al ministerio "MINISTERIO DE SALUD"
  Then el sistema no permite el envío del documento

Scenario: Funcionarios intenta enviar a Asesor de otro ministerio
  Given el usuario tiene el rol de "Funcionarios"
  And el usuario pertenece al ministerio "MINISTERIO DE DEFENSA"
  When selecciona como destinatario a "Asesor"
  And el destinatario pertenece al ministerio "MINISTERIO DE SALUD"
  Then el sistema no permite el envío del documento

Scenario: ONU intenta enviar a Ministro
  Given el usuario tiene el rol de "ONU"
  When selecciona como destinatario a "Ministro"
  Then el sistema no permite el envío del documento

Scenario: Prensa intenta enviar a Presidente
  Given el usuario tiene el rol de "Prensa"
  When selecciona como destinatario a "Presidente"
  Then el sistema no permite el envío del documento

Scenario: Funcionarios intenta enviar a Ministro
  Given el usuario tiene el rol de "Funcionarios"
  And el usuario pertenece al ministerio "MINISTERIO DE SALUD"
  When selecciona como destinatario a "Ministro"
  And el destinatario pertenece al ministerio "MINISTERIO DE SALUD"
  Then el sistema no permite el envío del documento

Scenario: Ministro intenta enviar a Mesa de Control
  Given el usuario tiene el rol de "Ministro"
  And el usuario pertenece al ministerio "MINISTERIO DE SALUD"
  When selecciona como destinatario a "Mesa de Control"
  Then el sistema no permite el envío del documento