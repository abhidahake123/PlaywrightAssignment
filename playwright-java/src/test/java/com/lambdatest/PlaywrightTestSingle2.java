package com.lambdatest;
import com.google.gson.JsonObject;
import com.microsoft.playwright.*;
import com.microsoft.playwright.options.BoundingBox;

import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;

public class PlaywrightTestSingle2 {
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
            try(Playwright playwright1 = Playwright.create()) {
                Browser browser1 = playwright1.chromium().launch(new BrowserType.LaunchOptions().setHeadless(false));
                BrowserContext context = browser1.newContext();
                Page page1 = context.newPage();

                // Open LambdaTest Selenium Playground
                page1.navigate("https://www.lambdatest.com/selenium-playground");
                
                System.out.println("1");

                // Click on "Drag & Drop Sliders"
                page1.locator("text=Drag & Drop Sliders").click();
                
                System.out.println("2");

                // Select the slider "Default value 15" and drag it to 95
                //page.locator("#slider").dragTo(500, 0);  // Adjust based on slider UI
                
                Locator rangeSlider = page1.locator("//input[@type='range' and @value='15']");
                
                // Get slider bounding box
                BoundingBox box = rangeSlider.boundingBox();
                if (box != null) {
                    double startX = box.x;
                    double centerY = box.y + (box.height / 2);
                    double endX = startX + (box.width * (box.width-0.05)); // Move to 95%
                    
                    // Drag slider to the 95% position
                    page.mouse().move(startX, centerY);
                    page.mouse().down();
                    page.mouse().move(endX, centerY);
                    page.mouse().up();
                }
                
                // Verify the new value
                String sliderValue = rangeSlider.inputValue();
                System.out.println("Slider Value: " + sliderValue);
                
                
                System.out.println("3");

                // Validate whether the range value shows 95
                String value = page1.locator("#rangeValue").textContent();
                assert value.equals("95");
                
                System.out.println("Execution Completed");

                browser1.close();
            }
        }
    }
}

