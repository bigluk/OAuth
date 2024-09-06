# OAuth

## Authentication Authorization
Authentication is the process of verifying a principal’s identity against what it claims to be. A principal can be anyone who is trying to access the application, such as a User, another program or API etc. When accessing a secured application, the principal must provide evidence of its identity which, generally, is a username and password combination. If the principal is another program, identity is confirmed by matching the provided API key and secret.

Authorization refers to the process of granting authority (typically the roles) to an authenticated principal and allowing access to particular secured resources. Authorization is always performed after the authentication process.

<img width="828" alt="slide 3" src="https://github.com/user-attachments/assets/51c8e834-055a-477f-8b6c-a88dea52c08c">


## How Spring without Spring Security works
Whenever we expose REST endpoints in Spring Boot, all the incoming requests are always first intercepted by the DispatcherServlet. The DispatcherServlet is the front controller in Spring web applications. The core responsibility of a DispatcherServlet is to dispatch incoming HttpRequests to the correct handlers.


<img width="945" alt="slide 1" src="https://github.com/user-attachments/assets/feab6b56-e00a-4bb8-8192-3f20441c9c15">

## How Spring with Spring Security works
To enable Spring security we have to just add the spring security dependency to the pom.xml.
Once we add the spring security dependency in the pom, during application startup, Spring registers a Filter named DelegatingFilterProxy. This bean will intercept every requests direct to and from the DispatcherServelet and apply on them a concatenantion of filter logic. It is here that authentication and authorization takes place.

<img width="880" alt="slide 2" src="https://github.com/user-attachments/assets/5bca5e43-a552-4110-8981-842616c8003e">

In other words, we can see the DelegatingFilterProxy as a "customs" that analize every request. If the request satisfy all the requirements, described with the filter chain, the DelegatingFilterProxy let the request pass to the DispatcherServelet. The following picture describe percfectly how the filter chain is composed: the pre-process and post-process filters and all the security filters configured in the DelegatingFilterProxy.

<img width="828" alt="slide 4" src="https://github.com/user-attachments/assets/7a4987ea-df2f-4a0a-bc20-47debd876eda">


Strictly speaking, Spring security is based on Servlet Filters. The core task of a Filter is to pre-process and post-process certain actions when a request is sent to a Servlet for actual request processing. The power of Spring is that we can customize this process as we want.


## How the authentication process works
The following diagrams demonstrate how the authentication flow happens at a high level:

<img width="933" alt="slide 5" src="https://github.com/user-attachments/assets/cb121ac8-2dda-47a8-949c-4619e32c1502">

Let's see each components in detail:

### Authentication Filter
A series of Spring Security filters intercept each request & work together to identify if Authentication is required or not. If authentication is NOT required then Filters does nothing, otherwise Filters attempt to convert the HttpServletRequest () into an Authentication object. After that, if the Authentication object is not empty, Filters invokes AuthenticationManager specified in AuthenticationFilter that is used to perform authentication.
The Filters are implemented in a configuration class that must have a Bean called filterChain with HttpSecurity as input
and SecurityFilterChain as output.


	@Configuration
	@EnableWebSecurity
	public class SecurityConfig {

    	@Bean
		public SecurityFilterChain filterChain(HttpSecurity http) throws Exception {
 			http
 				.csrf(Customizer.withDefaults())
 				.authorizeHttpRequests(authorize -> authorize
 				.anyRequest().authenticated()
 			)
 			.httpBasic(Customizer.withDefaults())
 			.formLogin(Customizer.withDefaults());
 			
 			return http.build();
 		}

	}


Most of the time, the default security filters are enough to provide security to your application. However, there might be times that you want to add a custom Filter to the security filter chain.
For example, let’s say that you want to add a Filter that gets a tenant id header and check if the current user has access to that tenant.
In order to do that we have to create a new class that implements Filter and override the doFilter method.


	import java.io.IOException;

	import jakarta.servlet.Filter;
	import jakarta.servlet.FilterChain;
	import jakarta.servlet.ServletException;
	import jakarta.servlet.ServletRequest;
	import jakarta.servlet.ServletResponse;
	import jakarta.servlet.http.HttpServletRequest;
	import jakarta.servlet.http.HttpServletResponse;

	import org.springframework.security.access.AccessDeniedException;

	public class TenantFilter implements Filter {

    	@Override
    	public void doFilter(ServletRequest servletRequest, ServletResponse servletResponse, FilterChain filterChain) throws IOException, ServletException {
        	HttpServletRequest request = (HttpServletRequest) servletRequest;
        	HttpServletResponse response = (HttpServletResponse) servletResponse;

        	String tenantId = request.getHeader("X-Tenant-Id"); (1)
        	boolean hasAccess = isUserAllowed(tenantId); (2)
        	if (hasAccess) {
            	filterChain.doFilter(request, response); (3)
            	return;
        	}
        	throw new AccessDeniedException("Access denied"); (4)
    	}

	}


The sample code above does the following:

	1. Get the tenant id from the request header.
	2. Check if the current user has access to the tenant id.
	3. If the user has access, then invoke the rest of the filters in the chain.
	4. If the user does not have access, then throw an AccessDeniedException.


[!NOTE]
Instead of implementing Filter, you can extend from OncePerRequestFilter which is a base class for filters that are only invoked once per request and provides a doFilterInternal method with the HttpServletRequest and HttpServletResponse parameters.

Now, we need to add the filter to the security filter chain.

	@Bean
	SecurityFilterChain filterChain(HttpSecurity http) throws Exception {
    	http
        	// ...
        	.addFilterBefore(new TenantFilter(), AuthorizationFilter.class);
    	return http.build();
	}


By adding the filter before the AuthorizationFilter we are making sure that the TenantFilter is invoked after the authentication filters. You can also use HttpSecurity.addFilterAfter to add the filter after a particular filter or HttpSecurity#addFilterAt to add the filter at a particular filter position in the filter chain.
And that’s it, now the TenantFilter will be invoked in the filter chain and will check if the current user has access to the tenant id.


### Authentication Manager
Once received request from filter, it delegates the validating of the user details to the authentication providers available. Since there can be multiple providers inside an app, it is the responsibility of the AuthenticationManager to manage all the authentication providers available.

### Authentication Provider
AuthenticationProviders has all the core logic of validating user details for authentication.

### User Details Service 
It helps in retrieving, creating, updating, deleting the User Details from the DB/storage systems.

### Password Encoder 
Service interface that helps in encoding & hashing passwords. Otherwise we may have to live with plain text passwords.


### Security Context 
Once the request has been authenticated, the Authentication will usually be stored in a thread-local SecurityContext managed by the SecurityContextHolder. This helps during the upcoming requests from the same user.



























