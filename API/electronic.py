#API return electronic item 

import requests
from bs4 import BeautifulSoup
import time
import json
import toolz
from selenium import webdriver
from selenium.webdriver.common.keys import Keys

class product:  
    def __init__(self, product_name, img_url, p_price, p_url ):  
        self.product_name = product_name  
        self.img_url = img_url
        self.p_price = p_price
        self.p_url = p_url
        
def euroRtv(productlist, input_name):
     
    url = "https://www.euro.com.pl/search.bhtml?keyword=" + input_name + "&searchType=Tag#fromKeyword=" + input_name
    
    page = requests.get(url)
    
    soup = BeautifulSoup(page.content, 'html.parser')
    
    for a in soup.find_all("div", {"class": "product-row"}):
        try:
            p_name = a.find("a", {"class":"js-save-keyword"}).text.strip().replace("\u200c","")
            p_img_url = a.find("div",{"class":"product-photo"}).a.img["src"]
            if(p_img_url == "/img/desktop/empty.png"):
                p_img_url = a.find("div",{"class":"product-photo"}).a.img["data-original"]
            p_url = a.find("a", {"class":"js-save-keyword"})["href"]
            p_price = a.find("div", {"class":"price-normal selenium-price-normal"}).text.strip().replace("\u200c","")[:-2] 
        except:
             return False
            
        productlist.append(product(p_name, p_img_url, p_price, "https://www.euro.com.pl/" + p_url))  

def mediaExpert(productlist, input_name):
         
    url = "https://www.mediaexpert.pl/search?query%5Bmenu_item%5D=&query%5Bquerystring%5D=" + input_name
    
    page = requests.get(url)
    
    soup = BeautifulSoup(page.content, 'html.parser')
    elements = soup.find_all("div", {"class": "c-grid_col is-grid-col-1"})
    for a in elements:
        
        try:
            p_name = a.find("h2", {"class":"c-offerBox_data"}).text.strip().replace("\u200c","") 
            try:
                p_img_url = a.find("div",{"class":"c-offerBox_photo"}).a.img["src"]
            except:
                p_img_url = a.find("div",{"class":"c-offerBox_photo"}).a.img["data-src"]
            
            p_price = a.find("div", {"class":"is-big"}).text[:-2]
            p_url = a.find("h2", {"class":"c-offerBox_data"}).a["href"]
            
        except:
            return False
            
        productlist.append(product(p_name, p_img_url, p_price, "https://www.mediaexpert.pl/" + p_url))  
        
        
def mediaMarkt(productlist, input_name):
         
    url = "https://mediamarkt.pl/" + input_name + "?search=querystring=" + input_name
    
    browser = webdriver.Chrome()
    #browser.maximize_window()
    browser.get(url)
    
    elements = browser.find_elements_by_class_name("m-offerBox_content")[:20]
    for a in elements:
        a.send_keys(Keys.PAGE_DOWN)
        time.sleep(0.2)
        
        try:    
            p_name = a.find_element_by_class_name("b-ofr_headDataTitle").text
    
            p_img_url = a.find_element_by_class_name("js-lazyLoad_img").get_attribute("src")
            
            p_price = a.find_element_by_class_name("m-priceBox_price").text.strip().replace("-", "00")[:-2]
            
            p_url = a.find_element_by_class_name("b-ofr_headDataTitle").get_attribute("href")
            
            
        except:
            print(elements.index(a))
            return False
            
        productlist.append(product(p_name, p_img_url, p_price, "https://mediamarkt.pl/" + p_url))                  
    browser.close()
    
    
def main(input_name):
    productlist = []
    euroRtv(productlist, input_name)
    mediaExpert(productlist, input_name)
    #mediaMarkt(productlist, input_name)
    
    
    productlist = toolz.unique(productlist, key=lambda x: x.product_name)

    json_string = json.dumps([ob.__dict__ for ob in productlist], ensure_ascii=False).encode('utf8')
    return json_string
 


