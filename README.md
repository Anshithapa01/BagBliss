<h1>BagBliss eCommerce Platform</h1>
    <p>BagBliss is a feature-rich eCommerce web application for selling bags. It is designed to provide a seamless shopping experience for users and a comprehensive management interface for administrators. Built using Java, Spring Boot, Hibernate, PostgreSQL, HTML, CSS, and JavaScript, BagBliss combines powerful backend functionality with a user-friendly frontend.</p>
    

   Features<br>
   User Side
    
        1.Home Page: Browse featured products and latest arrivals.
        2. Shopping Cart: Add, view, and manage products in the shopping cart.
        4. Wallet: Manage wallet balance and transactions.
        5. Wishlist: Save products for future purchase.
        6. Order Page: View order history and track order status.
        7. User Profile: Manage personal information and address details.
        8. Referral Earning: Earn rewards through referral programs.
        9. Coupon System: Apply discount coupons at checkout.
        10. Checkout: Secure and streamlined checkout process.
        11. Shop Page: Browse and search for products by categories and filters.
        12. Search and Filters: Find products easily using search and filter options.
    

    Admin Side
    
        Dashboard: Graphical representation of sales data (monthly, daily, yearly) and earnings by payment methods. Top 10 selling products and categories are also displayed.
        Order Management: Manage order statuses, download sales reports in PDF and CSV formats.
        Inventory Management: Automate inventory updates and manage product quantities.
        Customer Management: View and manage customer details.
        Offer Management: Create and manage promotional offers.
        Coupon Management: Generate and manage discount coupons.
        Product Management: Add, update, and delete product listings.

   Technologies Used
    
        Backend: Java, Spring Boot, Hibernate
        Database: PostgreSQL
        Frontend: HTML, CSS, JavaScript
        Security: Spring Security
        Payment Integration: COD, Razorpay, Wallet Payment

    Installation and Setup

        Clone the Repository:
            git clone https://github.com/Anshithapa01/bagbliss.git <br>cd bagbliss
        
        
        Backend Setup:
            
                Ensure you have Java and Maven installed.
                Configure your PostgreSQL database and update the application.properties file with your database details.
                Run the Spring Boot application:
                    mvn spring-boot:run
                
            
        Frontend Setup:
            
               Ensure you have Node.js and npm installed.
               Navigate to the frontend directory and install dependencies:
                cd frontend
                npm install
                npm start
                

Usage
  
        Access the user interface at http://localhost:8020/shop/.
        Access the admin interface at http://localhost:8019/admin/login.
        Register as admin at http://localhost:8019/admin/register.
    

    Future Enhancements
    
        Automated inventory management
        Integration of additional payment gateways
        Enhanced payment security
        Customer feedback system
        Integration with shipping services
   

    Contribution
    We welcome contributions! Please follow these steps:
    
        Fork the repository.
        Create a new branch (git checkout -b feature-branch).
        Make your changes and commit them (git commit -m 'Add new feature').
        Push to the branch (git push origin feature-branch)
        Open a Pull Request.
    
