/*******************************************************************************
 * Copyright (c) 2013 IBM Corp.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *    http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 *******************************************************************************/
package com.acmeair.web;

import com.acmeair.service.CustomerService;
import com.acmeair.service.ServiceLocator;
import com.acmeair.web.dto.CustomerInfo;

import javax.servlet.http.HttpServletRequest;
import javax.ws.rs.*;
import javax.ws.rs.core.Context;
import javax.ws.rs.core.Response;
import java.util.concurrent.ThreadPoolExecutor;
import java.util.concurrent.TimeUnit;
import java.util.logging.Level;
import java.util.logging.Logger;
import java.io.BufferedReader;
import java.io.InputStreamReader;

import java.net.URL;
import java.net.URLConnection;
import java.util.List;
import java.util.Map;

import java.io.File;  
import java.io.FileInputStream;  
import java.io.FileNotFoundException;  
import java.io.FileOutputStream;  
import java.io.IOException;  
import java.io.InputStream;  
import java.io.OutputStream; 



@Path("/customer")
public class CustomerREST {

	private static int poolSize = 8;
	private static ThreadPoolExecutor executor = new ThreadPoolExecutor(poolSize, poolSize, 200, TimeUnit.MILLISECONDS, new QueueTest<Runnable>(200));
	private static int c;
	static int index=0;

	private CustomerService customerService = ServiceLocator.instance().getService(CustomerService.class);

	@Context
	private HttpServletRequest request;


	private boolean validate(String customerid)	{
		String loginUser = (String) request.getAttribute(RESTCookieSessionFilter.LOGIN_USER);
		if(logger.isLoggable(Level.FINE)){
			logger.fine("validate : loginUser " + loginUser + " customerid " + customerid);
		}
		return customerid.equals(loginUser);
	}

	protected Logger logger =  Logger.getLogger(CustomerService.class.getName());

	@GET
	@Path("/byid/{custid}")
	@Produces("text/plain")
	public void getCustomer(@CookieParam("sessionid") String sessionid, @PathParam("custid") String customerid, @QueryParam("sendtime") String sendtime) {

		/*MyTask myTask = new MyTask(index++,sessionid,customerid,sendtime);
		System.out.println(System.currentTimeMillis()+"start task: "+index);
		executor.execute(myTask);
		System.out.println("poolSize: "+executor.getPoolSize()+" , queueWaitSize: "+
				executor.getQueue().size()+" , finishTask: "+executor.getCompletedTaskCount());
*/
		
		String param = "uid0@email.com";
	    String url = "http://192.168.0.88/customer/acmeair-cs1/rest/api/customer/byid/" + param;
	    
	    String result = "";
        BufferedReader in = null;
        try {
            URL realUrl = new URL(url);
            // 鎵撳紑鍜孶RL涔嬮棿鐨勮繛鎺�
            URLConnection connection = realUrl.openConnection();
            // 璁剧疆閫氱敤鐨勮姹傚睘鎬�
            connection.setRequestProperty("Cookie","sessionid="+sessionid);
            connection.setRequestProperty("accept", "*/*");
            connection.setRequestProperty("connection", "Keep-Alive");
            connection.setRequestProperty("User-Agent",
                    "Mozilla/5.0 (Windows NT 6.1; WOW64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/55.0.2883.87 Safari/537.36");
            // 寤虹珛瀹為檯鐨勮繛鎺�
//            conn.setDoOutput(true);
            connection.connect();
//            
            OutputStream out = connection.getOutputStream();  //java.net.ProtocolException
            out.write("We are Chinese.".getBytes());
            // 鑾峰彇鎵�鏈夊搷搴斿ご瀛楁
            Map<String, List<String>> map = connection.getHeaderFields();
            // 閬嶅巻鎵�鏈夌殑鍝嶅簲澶村瓧娈�
            for (String key : map.keySet()) {
                System.out.println(key + "--->" + map.get(key));
            }
            // 瀹氫箟 BufferedReader杈撳叆娴佹潵璇诲彇URL鐨勫搷搴�
            in = new BufferedReader(new InputStreamReader(
                    connection.getInputStream()));
            String line;
            while ((line = in.readLine()) != null) {
                result += line;
            }
        } catch (Exception e) {
            System.out.println("鍙戦�丟ET璇锋眰鍑虹幇寮傚父锛�" + e);
            e.printStackTrace();
        }
        // 浣跨敤finally鍧楁潵鍏抽棴杈撳叆娴�
        finally {
            try {
                if (in != null) {
                    in.close();
                }
            } catch (Exception e2) {
                e2.printStackTrace();
            }
        }
//       return result;
        
		
	}

	class MyTask implements Runnable {
		private int taskNum;
		private String sessionid;
		private String customerid;
		private String sendtime;

		public MyTask(int num,String sessionid,String customerid,String sendtime) {
			this.taskNum = num;
			this.sessionid=sessionid;
			this.customerid=customerid;
			this.sendtime=sendtime;
		}

		@Override
		public void run() {
			System.out.println("executing task "+taskNum);
			try {
				getInfo(sessionid,customerid,sendtime);
			} catch (Exception e) {
				e.printStackTrace();
			}
			System.out.println("task "+taskNum+"finished");
		}

		public void getInfo(String sessionid,String customerid,String sendtime){
			System.out.println("send time: " + sendtime);
			if(logger.isLoggable(Level.FINE)){
				logger.fine("getCustomer : session ID " + sessionid + " userid " + customerid);
			}
			try {
				// make sure the user isn't trying to update a customer other than the one currently logged in
				if (!validate(customerid)) {
					System.out.println("error");
				}
				//System.out.println(customerService.getCustomerByUsername(customerid));
				String[] customerIds = {customerid,"uid1@email.com","uid2@email.com"};
				System.out.println(customerService.getCustomersByUsernames(customerIds));
			}
			catch (Exception e) {
				e.printStackTrace();
			}
		}
	}



	@POST
	@Path("/byid/{custid}")
	@Produces("text/plain")
	public Response putCustomer(@CookieParam("sessionid") String sessionid, CustomerInfo customer) {
		String username = customer.getUsername();
		if (!validate(username)) {
			return Response.status(Response.Status.FORBIDDEN).build();
		}

		String customerFromDB = customerService.getCustomerByUsernameAndPassword(username, customer.getPassword());
		if(logger.isLoggable(Level.FINE)){
			logger.fine("putCustomer : " + customerFromDB);
		}

		if (customerFromDB == null) {
			// either the customer doesn't exist or the password is wrong
			return Response.status(Response.Status.FORBIDDEN).build();
		}

		customerService.updateCustomer(username, customer);

		//Retrieve the latest results
		customerFromDB = customerService.getCustomerByUsernameAndPassword(username, customer.getPassword());
		return Response.ok(customerFromDB).build();
		
		
    
	    
	}

	@POST
	@Path("/validateid")
	@Consumes({"application/x-www-form-urlencoded"})
	@Produces("text/plain")
	public Response validateCustomer(@FormParam("login") String login, @FormParam("password") String password) {
		if(logger.isLoggable(Level.FINE)){
			logger.fine("validateid : login " + login + " password " + password);
		}

		String validCustomer = null;

		if (customerService.validateCustomer(login, password)) {
			validCustomer = "true";
		} else {
			validCustomer = "false";
		}

		String s = "{\"validCustomer\":\"" + validCustomer + "\"}";

		return Response.ok(s).build();
	}



}
