package com.tele.url_short.controller;

import java.util.List;
import java.util.Random;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;

import com.tele.url_short.dto.MyUrl;
import com.tele.url_short.dto.User;
import com.tele.url_short.repository.UrlRepository;
import com.tele.url_short.repository.UserRepository;

@Controller
public class MyController {

    @Autowired
    UserRepository userRepository;

    @Autowired
    UrlRepository urlRepository;

    @GetMapping({ "/", "/login" })
    public String login() {
        return "login";
    }

    @GetMapping("/signup")
    public String signup() {
        return "signup";
    }

    @PostMapping("/signup")
    public String signup(User user, ModelMap map) {
        if (!userRepository.existsByEmail(user.getEmail())) {
            userRepository.save(user);
            map.put("pos", "Account Created Success");
            return "login";
        } else {
            map.put("neg", "Account Already Exsists");
            return "signup";
        }
    }

    @PostMapping("/login")
    public String login(@RequestParam String email, @RequestParam String password, ModelMap map) {
        User user = userRepository.findByEmail(email);
        if (user == null) {
            map.put("neg", "Invalid Email");
            return "login";
        } else {
            if (user.getPassword().equals(password)) {
                return "redirect:/home";
            } else {
                map.put("neg", "Invalid Password");
                return "login";
            }
        }
    }

    @GetMapping("/add-url")
    public String addUrl() {
        return "url";
    }

    @GetMapping("/logout")
    public String logout(ModelMap map) {
        map.put("pos", "Logout Success");
        return "login";
    }

    @GetMapping("/home")
    public String loadHome(ModelMap map) {
        List<MyUrl> list=urlRepository.findAll();
        map.put("list", list);

        return "dashboard";
    }
    

    @PostMapping("/shorten")
    public String shortenUrl(@RequestParam("url") String longUrl,ModelMap map) {

        MyUrl url = new MyUrl();
        url.setLongUrl(longUrl);

        boolean flag = true;
        while (flag) {
            String shortUrl = Integer.toHexString(new Random().nextInt());
            if (!urlRepository.existsByShortUrl(shortUrl)) {
                url.setShortUrl(shortUrl);
                flag = false;
            }
        }
        urlRepository.save(url);
        map.put("pos", "Url Shortened Success");
       return "redirect:/home";
    }

    @GetMapping("/{url}")
    public String redirectUrl(@PathVariable String url,ModelMap map){
        MyUrl myUrl=urlRepository.findByShortUrl(url);
        if(myUrl!=null){
            myUrl.setUrlCount(myUrl.getUrlCount()+1);
            urlRepository.save(myUrl);
            return "redirect:"+myUrl.getLongUrl();
        }
        else{
            map.put("neg", "Invalid Url");
            List<MyUrl> list=urlRepository.findAll();
            map.put("list", list);
            return "dashboard";
        }
    }
}