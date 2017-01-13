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

import java.io.File;  
import java.io.FileInputStream;  
import java.io.FileNotFoundException;  
import java.io.FileOutputStream;  
import java.io.IOException;  
import java.io.InputStream;  
import java.io.OutputStream; 

@Path("/customer")
public class CustomerREST1 {
	public int test(URLConnection connection){
		InputStream in = connection.getInputStream();
         StringBuffer sb = new StringBuffer();
		 byte[] arr = new byte[10240];
		 String len;
		 while ((len = in.read(arr)) > 0) {
			sb.append(new String(arr, 0, len));
		 }


		 System.out.println(sb.toString());
		
		
		//System.out.println("ok");
		return 0;
	}
	



}
