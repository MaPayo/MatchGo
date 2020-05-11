Feature: As a registered user I would like to login to the system using my username and password.

original Feature titile: csrf and sign-in end point

Background:
* url baseUrl
* def util = Java.type('karate.KarateTests')

# chromium bajo linux; si usas google-chrome, puedes quitar executable (que es lo que usaría por defecto)
* configure driver = { type: 'chrome', executable: '/usr/bin/chromium-browser', showDriverLog: true }

Given path 'login'
When method get
Then status 200
* string response = response    
* def csrf = util.selectAttribute(response, "input[name=_csrf]", "value");
* print csrf

# selectores para util.select*: ver https://jsoup.org/cookbook/extracting-data/selector-syntax
# objetivo de la forma
# <html lang="en">...<body><div><form>
#   <input name="_csrf" type="hidden" value="..." />

Scenario: Successful Login
#html url encoded form submit - post
    Given path 'login'
    And form field userName = 'n3'
    And form field password = 'aa'
    And form field _csrf = csrf
    When method post
    Then status 200
    * string response = response    
    * def result = util.selectHtml(response, "div.intereses_usuario > h2");
    * print result
    And match result contains 'Intereses:'
