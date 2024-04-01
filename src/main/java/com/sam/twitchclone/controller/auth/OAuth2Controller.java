package com.sam.twitchclone.controller.auth;

import lombok.RequiredArgsConstructor;
import org.springframework.security.oauth2.client.OAuth2AuthorizedClient;
import org.springframework.security.oauth2.client.OAuth2AuthorizedClientService;
import org.springframework.security.oauth2.client.authentication.OAuth2AuthenticationToken;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.servlet.view.RedirectView;

@RestController
@RequiredArgsConstructor
public class OAuth2Controller {
    private final OAuth2AuthorizedClientService clientService;

    @GetMapping("/login/oauth2/code/{provider}")
    public RedirectView loginSuccess(@PathVariable String provider, OAuth2AuthenticationToken authenticationToken) {
        OAuth2AuthorizedClient client = clientService.loadAuthorizedClient(
                authenticationToken.getAuthorizedClientRegistrationId(),
                authenticationToken.getName()
        );


        String userEmail = client.toString();
        String providerId = authenticationToken.getAuthorizedClientRegistrationId();

        System.out.println("user email " +userEmail);
        System.out.println("provider " + provider + ", " + providerId);

        // Handle storing the user information and token, then redirect to a successful login page
        // For example, store user details in your database and generate a JWT token for further authentication.

        return new RedirectView("/movies/dashboard");
    }

}
