package com.hanbit.controller;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.core.io.ClassPathResource;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.HashMap;
import java.util.List;
import java.util.Map;


@RestController("/api/v1/data/currency")
public class HomeController {

    public HashMap getKRW(String key) {
        HashMap retHash = new HashMap<>();

        ClassPathResource resource = new ClassPathResource("price.json");
        List<String> content = null;
        try {
            Path path = Paths.get(resource.getURI());
            content = Files.readAllLines(path);

            ObjectMapper mapper = new ObjectMapper();
            JsonNode actualObj = mapper.readTree(content.get(0).toString());

            JsonNode bchnode = actualObj.path(key);
            JsonNode bithumbnode = bchnode.path("bithumb");
            String originPair1 = bithumbnode.path("originPair").textValue();
            String last1 = bithumbnode.path("last").textValue();

            JsonNode coinonenode = bchnode.path("coinone");
            String originPair2 = coinonenode.path("originPair").textValue();
            String last2 = coinonenode.path("last").textValue();

            JsonNode korbitnode = bchnode.path("korbit");
            String originPair3 = korbitnode.path("originPair").textValue();
            String last3 = korbitnode.path("last").textValue();

            JsonNode bitfinexnode = bchnode.path("bitfinex");
            String originPair4 = bitfinexnode.path("originPair").textValue();
            String last4 = bitfinexnode.path("last").textValue();

            Map<String,String> bithumb = new HashMap<String,String>();
            bithumb.put("originPair", originPair1);
            bithumb.put("last", last1);
            Map<String,String> coinone = new HashMap<String,String>();
            coinone.put("originPair", originPair2);
            coinone.put("last", last2);
            Map<String,String> bitfinex = new HashMap<String,String>();
            bitfinex.put("originPair", originPair4);
            bitfinex.put("last", last4);
            Map<String,String> korbit = new HashMap<String,String>();
            bitfinex.put("originPair", originPair3);
            bitfinex.put("last", last3);

            retHash.put("bithumb", bithumb);
            retHash.put("korbit", korbit);
            retHash.put("coinone", coinone);
            retHash.put("bitfinex", bitfinex);

            System.out.println("getKRW:::::" + retHash.toString());

        } catch (IOException e) {

        }

        return retHash;
    }

    @RequestMapping
    public @ResponseBody
    String index() {
        String[] params = {"BTC", "EOS", "ETH", "BCH", "XRP"};
        HashMap<String,Object> obj = new HashMap<String,Object>();
        for (int i = 0; i < params.length; i++) {
            String key = params[i]+"_KRW";
            obj.put(key, getKRW(key));
        }

        HashMap<String,Object> map = new HashMap<String,Object>();
        map.put("data", obj);
        map.put("status", "success");

        String ret = null;

        ObjectMapper mapper = new ObjectMapper();
        try {
            ret = mapper.writeValueAsString(map);
            ret = mapper.writerWithDefaultPrettyPrinter().writeValueAsString(map);
            System.out.println(ret);
        } catch (JsonProcessingException e) {
            e.printStackTrace();
        }

        return ret;
    }

    @RequestMapping("/api/v1/data/currency/{currency}")
    public @ResponseBody
    String showCurrency(@PathVariable String currency){

        HashMap<String,Object> obj = new HashMap<String,Object>();
        String key = currency+"_KRW";
        obj.put(key, getKRW(key));

        HashMap<String,Object> map = new HashMap<String,Object>();
        map.put("data", obj);
        map.put("status", "success");

        String ret = null;

        ObjectMapper mapper = new ObjectMapper();
        try {
            ret = mapper.writeValueAsString(map);
            ret = mapper.writerWithDefaultPrettyPrinter().writeValueAsString(map);
            System.out.println(ret);
        } catch (JsonProcessingException e) {
            e.printStackTrace();
        }

        return ret;
    }
}
