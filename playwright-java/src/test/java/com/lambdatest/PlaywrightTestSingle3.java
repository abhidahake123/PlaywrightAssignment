package com.lambdatest;
import com.google.gson.JsonObject;
import com.microsoft.playwright.*;
import com.microsoft.playwright.options.SelectOption;

import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;

public class PlaywrightTestSingle3 {
    public static void main(String[] args) throws UnsupportedEncodingException {
        try (Playwright playwright = Playwright.create()) {
            JsonObject capabilities = new JsonObject();
            JsonObject ltOptions = new JsonObject();

            String user = "abhi.dahake1";
            String accessKey = "fI6JmAFHQWWbpxboKKdPTcH1HEtrRfEnwmQIyuFyGLiicTu4SL";

            capabilities.addProperty("browsername", "Chrome"); // Browsers allowed: `Chrome`, `MicrosoftEdge`, `pw-chromium`, `pw-firefox` and `pw-webkit`
            capabilities.addProperty("browserVersion", "latest");
            ltOptions.addProperty("platform", "Windows 10");
            ltOptions.addProperty("name", "Playwright Test");
            ltOptions.addProperty("build", "Playwright Java Build");
            ltOptions.addProperty("user", user);
            ltOptions.addProperty("accessKey", accessKey);
            capabilities.add("LT:Options", ltOptions);

            BrowserType chromium = playwright.chromium();
            String caps = URLEncoder.encode(capabilities.toString(), "utf-8");
            String cdpUrl = "wss://cdp.lambdatest.com/playwright?capabilities=" + capabilities;
            Browser browser = chromium.connect(cdpUrl);
            Page page = browser.newPage();
            try  (Playwright playwright1 = Playwright.create()) {
                Browser browser1 = playwright1.chromium().launch(new BrowserType.LaunchOptions().setHeadless(false));
                BrowserContext context = browser1.newContext();
                Page page1 = context.newPage();

             // Open the LambdaTest Selenium Playground
                page1.navigate("https://www.lambdatest.com/selenium-playground");
                
                System.out.println("1st");

                // Click on "Input Form Submit"
                page1.locator("text=Input Form Submit").click();

                System.out.println("1st");
                
                // Click "Submit" without filling in any information
                page1.locator("button.selenium_btn:text('Submit')").click();

                System.out.println("2");
                
                Page nameField = null;
				// Assert "Please fill out this field." error message
                String validationMessage = nameField.evaluate("el => el.validationMessage").toString();

                // Assert validation message
                assert validationMessage.equals("Please fill out this field.") : "Validation message did not match!";

                System.out.println("Validation message: " + validationMessage);

                System.out.println("3");
                
                // Fill in Name, Email, and other fields
                page1.locator("input[name='name']").fill("John Doe");
                page1.locator("input[name='email']").fill("john.doe@example.com");

                System.out.println("4");
                
                // Select "United States" from the Country drop-down
                page1.locator("select[name='country']").selectOption(new SelectOption().setLabel("United States"));

                System.out.println("5");
                
                // Click "Submit"
                page1.locator("#submit").click();

                // Validate success message
                page1.locator("text=Thanks for contacting us, we will get back to you shortly.").waitFor();

                System.out.println("6");

                browser1.close();
            }
        }
    }
}

