<h1>BagBliss eCommerce Platform</h1>
    <p>BagBliss is a feature-rich eCommerce web application for selling bags. It is designed to provide a seamless shopping experience for users and a comprehensive management interface for administrators. Built using Java, Spring Boot, Hibernate, PostgreSQL, HTML, CSS, and JavaScript, BagBliss combines powerful backend functionality with a user-friendly frontend.</p>
    
    <h2>Features</h2>
    <h3>User Side</h3>
    <ul>
        <li><strong>Home Page</strong>: Browse featured products and latest arrivals.</li>
        <li><strong>Shopping Cart</strong>: Add, view, and manage products in the shopping cart.</li>
        <li><strong>Wallet</strong>: Manage wallet balance and transactions.</li>
        <li><strong>Wishlist</strong>: Save products for future purchase.</li>
        <li><strong>Order Page</strong>: View order history and track order status.</li>
        <li><strong>User Profile</strong>: Manage personal information and address details.</li>
        <li><strong>Referral Earning</strong>: Earn rewards through referral programs.</li>
        <li><strong>Coupon System</strong>: Apply discount coupons at checkout.</li>
        <li><strong>Checkout</strong>: Secure and streamlined checkout process.</li>
        <li><strong>Shop Page</strong>: Browse and search for products by categories and filters.</li>
        <li><strong>Search and Filters</strong>: Find products easily using search and filter options.</li>
    </ul>

    <h3>Admin Side</h3>
    <ul>
        <li><strong>Dashboard</strong>: Graphical representation of sales data (monthly, daily, yearly) and earnings by payment methods. Top 10 selling products and categories are also displayed.</li>
        <li><strong>Order Management</strong>: Manage order statuses, download sales reports in PDF and CSV formats.</li>
        <li><strong>Inventory Management</strong>: Automate inventory updates and manage product quantities.</li>
        <li><strong>Customer Management</strong>: View and manage customer details.</li>
        <li><strong>Offer Management</strong>: Create and manage promotional offers.</li>
        <li><strong>Coupon Management</strong>: Generate and manage discount coupons.</li>
        <li><strong>Product Management</strong>: Add, update, and delete product listings.</li>
    </ul>

    <h2>Technologies Used</h2>
    <ul>
        <li><strong>Backend</strong>: Java, Spring Boot, Hibernate</li>
        <li><strong>Database</strong>: PostgreSQL</li>
        <li><strong>Frontend</strong>: HTML, CSS, JavaScript</li>
        <li><strong>Security</strong>: Spring Security</li>
        <li><strong>Payment Integration</strong>: COD, Razorpay, Wallet Payment</li>
    </ul>

    <h2>Installation and Setup</h2>
    <ol>
        <li><strong>Clone the Repository</strong>:
            <pre><code>git clone https://github.com/Anshithapa01/bagbliss.git
cd bagbliss</code></pre>
        </li>
        <li><strong>Backend Setup</strong>:
            <ul>
                <li>Ensure you have Java and Maven installed.</li>
                <li>Configure your PostgreSQL database and update the application.properties file with your database details.</li>
                <li>Run the Spring Boot application:
                    <pre><code>mvn spring-boot:run</code></pre>
                </li>
            </ul>
        </li>
        <li><strong>Frontend Setup</strong>:
            <ul>
                <li>Ensure you have Node.js and npm installed.</li>
                <li>Navigate to the frontend directory and install dependencies:
                    <pre><code>cd frontend
npm install
npm start</code></pre>
                </li>
            </ul>
        </li>
    </ol>

    <h2>Usage</h2>
    <ul>
        <li>Access the user interface at <code>http://localhost:8020/shop/</code>.</li>
        <li>Access the admin interface at <code>http://localhost:8019/admin/login</code>.</li>
        <li>Register as admin at <code>http://localhost:8019/admin/register</code>.</li>
    </ul>

    <h2>Future Enhancements</h2>
    <ul>
        <li>Automated inventory management</li>
        <li>Integration of additional payment gateways</li>
        <li>Enhanced payment security</li>
        <li>Customer feedback system</li>
        <li>Integration with shipping services</li>
    </ul>

    <h2>Contribution</h2>
    <p>We welcome contributions! Please follow these steps:</p>
    <ol>
        <li>Fork the repository.</li>
        <li>Create a new branch (<code>git checkout -b feature-branch</code>).</li>
        <li>Make your changes and commit them (<code>git commit -m 'Add new feature'</code>).</li>
        <li>Push to the branch (<code>git push origin feature-branch</code>).</li>
        <li>Open a Pull Request.</li>
    </ol>
