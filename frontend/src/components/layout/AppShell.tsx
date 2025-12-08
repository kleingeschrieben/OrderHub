import type { ReactNode } from "react";
import CartDropdown from "../cart/CartDropdown.tsx";
import type { AuthState } from "../../models/AuthCredentials.ts";
import type { CartItem } from "../../models/CartItem.ts";

type AppShellProps = {
    auth: AuthState;
    isAdmin: boolean;
    activePage: "products" | "orders";
    onChangePage: (page: "products" | "orders") => void;
    cartItems: CartItem[];
    cartOpen: boolean;
    onToggleCart: () => void;
    onIncrease: (productId: number) => void;
    onDecrease: (productId: number) => void;
    onCheckout: () => void;
    onLogout: () => void;
    children: ReactNode;
};

function AppShell(props: AppShellProps) {
    const { auth, isAdmin, activePage, onChangePage, cartItems, cartOpen, onToggleCart, onIncrease, onDecrease, onCheckout, onLogout, children } = props;

    return (
        <>
            <nav className="navbar navbar-expand-lg navbar-dark bg-dark">
                <div className="container">
                    <span className="navbar-brand">OrderHub</span>
                    <div className="collapse navbar-collapse" id="navbarNav">
                        <ul className="navbar-nav">
                            <li className="nav-item">
                                <a className={`nav-link${activePage === "products" ? " active" : ""}`} onClick={() => onChangePage("products")} aria-current="page">Products</a>
                            </li>
                            <li className="nav-item">
                                <a className={`nav-link${activePage === "orders" ? " active" : ""}`} onClick={() => onChangePage("orders")}>Orders</a>
                            </li>
                        </ul>
                    </div>
                    <div className="ms-auto d-flex align-items-center">
                        <CartDropdown items={cartItems} open={cartOpen} onToggle={onToggleCart} onIncrease={onIncrease} onDecrease={onDecrease} onCheckout={onCheckout} />
                        <span className="navbar-text me-3">Logged in as <strong>{auth!.username}</strong>{isAdmin && " (admin)"}</span>
                        <button className="btn btn-outline-light btn-sm" onClick={onLogout}>Logout</button>
                    </div>
                </div>
            </nav>
            <main className="container mt-4">
                {children}
            </main>
        </>
    );
}

export default AppShell;
