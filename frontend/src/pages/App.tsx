import { useState } from "react";
import AppShell from "../components/layout/AppShell.tsx";
import LoginPage from "../components/auth/LoginPage.tsx";
import type { AuthState } from "../models/AuthCredentials.ts";
import type { ProductResponse } from "../generated-api";
import { OrdersService, OpenAPI } from "../generated-api";
import ProductsView from "../components/products/ProductsView.tsx";
import OrdersView from "../components/orders/OrdersView.tsx";
import type { CartItem } from "../models/CartItem.ts";

function App() {
    const [auth, setAuth] = useState<AuthState | null>(null);
    const [activePage, setActivePage] = useState<"products" | "orders">("products");
    const [cartItems, setCartItems] = useState<CartItem[]>([]);
    const [cartOpen, setCartOpen] = useState(false);

    function handleLogin(authState: AuthState) {
        setAuth(authState);
    }

    function handleLogout() {
        setAuth(null);
        OpenAPI.USERNAME = undefined;
        OpenAPI.PASSWORD = undefined;
    }

    if (!auth) {
        return <LoginPage onLogin={handleLogin} />;
    }

    const isAdmin = auth.roles.includes("ROLE_ADMIN");

    function handleAddToCart(product: ProductResponse) {
        const stock = product.stock ?? 0;
        if (stock <= 0) return;
        setCartItems(prev => {
            const existing = prev.find(item => item.product.id === product.id);
            if (existing) {
                if (existing.quantity >= stock) return prev;
                return prev.map(item => item.product.id === product.id ? { ...item, quantity: item.quantity + 1 } : item);
            }
            return [...prev, { product, quantity: 1 }];
        });
    }

    function handleIncrease(productId: number) {
        setCartItems(prev => prev.map(item => {
            if (item.product.id !== productId) return item;
            const stock = item.product.stock ?? 0;
            if (stock > 0 && item.quantity >= stock) return item;
            return { ...item, quantity: item.quantity + 1 };
        }));
    }

    function handleDecrease(productId: number) {
        setCartItems(prev => prev.map(item => item.product.id === productId ? { ...item, quantity: item.quantity - 1 } : item).filter(item => item.quantity > 0));
    }

    async function handleCheckout() {
        if (cartItems.length === 0) return;
        const orderRequest = {
            items: cartItems.map(item => ({
                productId: item.product.id!,
                quantity: item.quantity
            }))
        };
        await OrdersService.createOrder(orderRequest);
        setCartItems([]);
        setCartOpen(false);
        setActivePage("orders");
    }

    const view = activePage === "products" ? <ProductsView onAddToCart={handleAddToCart} isAdmin={isAdmin} /> : <OrdersView isAdmin={isAdmin} />;

    return (
        <AppShell auth={auth} isAdmin={isAdmin} activePage={activePage} onChangePage={setActivePage} cartItems={cartItems} cartOpen={cartOpen} onToggleCart={() => setCartOpen(prev => !prev)} onIncrease={handleIncrease} onDecrease={handleDecrease} onCheckout={handleCheckout} onLogout={handleLogout}>
            {view}
        </AppShell>
    );
}

export default App;
