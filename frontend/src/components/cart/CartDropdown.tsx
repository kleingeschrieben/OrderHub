import type {CartItem} from "../../models/CartItem.ts";

type CartDropdownProps = {
    items: CartItem[];
    open: boolean;
    onToggle: () => void;
    onIncrease: (productId: number) => void;
    onDecrease: (productId: number) => void;
    onCheckout: () => void;
};

function CartDropdown({ items, open, onToggle, onIncrease, onDecrease, onCheckout }: CartDropdownProps) {
    const totalQuantity = items.reduce((sum, item) => sum + item.quantity, 0);
    const totalPrice = items.reduce((sum, item) => sum + (item.product.price ?? 0) * item.quantity, 0);

    return (
        <div className="dropdown me-3">
            <button type="button" className="btn btn-outline-light btn-sm dropdown-toggle d-flex align-items-center" onClick={onToggle}>
                <span className="me-1">🛒</span>
                <span>Cart</span>
                {totalQuantity > 0 && <span className="badge bg-light text-dark ms-2">{totalQuantity}</span>}
            </button>
            <div className={`dropdown-menu dropdown-menu-end p-3 ${open ? "show" : ""}`} style={{ minWidth: "260px" }}>
                {items.length === 0 && <span className="text-muted">No items</span>}
                {items.length > 0 && (
                    <>
                        <ul className="list-unstyled mb-2">
                            {items.map((item) => {
                                const stock = item.product.stock ?? 0;
                                const maxReached = stock > 0 && item.quantity >= stock;
                                return (
                                    <li key={item.product.id} className="d-flex justify-content-between align-items-center mb-2">
                                        <div>
                                            <div>{item.product.name}</div>
                                            <div className="small text-muted">{item.product.price?.toFixed(2)} € × {item.quantity}</div>
                                        </div>
                                        <div className="d-flex align-items-center">
                                            <button type="button" className="btn btn-outline-dark btn-sm" onClick={() => onDecrease(item.product.id!)}>−</button>
                                            <span className="px-2">{item.quantity}</span>
                                            <button type="button" className="btn btn-outline-dark btn-sm" onClick={() => onIncrease(item.product.id!)} disabled={maxReached}>+</button>
                                        </div>
                                    </li>
                                );
                            })}
                        </ul>
                        <div className="d-flex justify-content-between border-top pt-2 mb-2">
                            <strong>Total</strong>
                            <strong>{totalPrice.toFixed(2)} €</strong>
                        </div>
                        <button type="button" className="btn btn-primary w-100" onClick={onCheckout}>Create order</button>
                    </>
                )}
            </div>
        </div>
    );
}

export default CartDropdown;
