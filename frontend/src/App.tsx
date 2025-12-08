import { useState } from "react";
import LoginPage from "./LoginPage";
import type {AuthState} from "./models/auth.ts";
import ProductsView from "./ProductsView.tsx";
import OrdersView from "./OrdersView.tsx";

function App() {
    const [auth, setAuth] = useState<AuthState>({username:"admin", password:"admin"});
    const [activePage, setActivePage] = useState<"products" | "orders">("products");

    function handleLogout() {
        setAuth(null);
    }

    if (!auth) {
        return <LoginPage onLogin={setAuth}/>;
    }

    function renderView() {
        switch (activePage) {
            case "products":
                return <ProductsView/>;
            case "orders":
                return <OrdersView/>;
            default:
                return <ProductsView/>;
        }
    }

    return (
        <>
            <nav className="navbar navbar-expand-lg navbar-dark bg-dark">
                <div className="container">
                    <span className="navbar-brand">OrderHub</span>
                    <div className="collapse navbar-collapse" id="navbarNav">
                        <ul className="navbar-nav">
                            <li className="nav-item">
                                <a className="nav-link" onClick={() => setActivePage("products")} aria-current="page">Products</a>
                            </li>
                            <li className="nav-item">
                                <a className="nav-link" onClick={() => setActivePage("orders")}>Orders</a>
                            </li>
                        </ul>
                    </div>
                    <div className="ms-auto d-flex align-items-center">
                        <span className="navbar-text me-3">
                            Logged in as <strong>{auth.username}</strong>
                        </span>
                        <button className="btn btn-outline-light btn-sm" onClick={handleLogout}>
                            Logout
                        </button>
                    </div>
                </div>
            </nav>

            <main className="container mt-4">
                {renderView()}
            </main>
        </>
    );
}

export default App;
