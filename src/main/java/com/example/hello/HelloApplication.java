package com.example.hello;

import com.sendgrid.Content;
import com.sendgrid.Email;
import com.sendgrid.Mail;
import com.sendgrid.Method;
import com.sendgrid.Request;
import com.sendgrid.Response;
import com.sendgrid.SendGrid;
import java.io.IOException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;

@Controller
@SpringBootApplication

public class HelloApplication {
    
	public static void main(String[] args) {
		SpringApplication.run(HelloApplication.class, args);
	}
        
        @GetMapping("/")        
        public String helloForm(
                Model model) {
        model.addAttribute("hello", new Hello());
        return "index";
        }
        
        private SendGrid sendGrid;
        @Value("${sendgrid.apiKey}")
        private String apiKey;
        
        @PostMapping()
        public String greetingSubmit(@ModelAttribute Hello hello,Model model){
            System.out.println(apiKey);
        sendGrid = new SendGrid(apiKey);
        Email from=new Email(hello.getemail(),"No-reply");
        String subject = "お問合せありがとうございました";
        Email to = new Email(hello.getemail());
        Content content = new Content("text/html", hello.getmessage());
        Mail mail = new Mail(from, subject, to, content);
        mail.setTemplateId("f43e726f-ff03-4b39-a383-74b19946d02f");
        
        model.addAttribute("name", hello.getname()); 
        model.addAttribute("email", hello.getemail());
        model.addAttribute("message", hello.getmessage());
        model.addAttribute("tel", hello.gettel());  
          
        Request request = new Request();
        Response ret = null;
        try {
            request.method = Method.POST;
            request.endpoint = "mail/send";
            request.body = mail.build();
            ret = sendGrid.api(request);
        } catch (IOException ex) {
        }
        return "index_result";
        }
}
