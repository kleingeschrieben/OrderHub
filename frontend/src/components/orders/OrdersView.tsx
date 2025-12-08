import { type OrderResponse, OrdersService } from "../../generated-api";
import { useEffect, useState } from "react";
import PaginationBar from "../utils/PaginationBar.tsx";

type OrdersViewProps = {
    isAdmin: boolean;
};

function OrdersView({ isAdmin }: OrdersViewProps) {
    const [orders, setOrders] = useState<OrderResponse[]>([]);
    const [page, setPage] = useState(0);
    const pageSize = 10;

    useEffect(() => {
        let cancelled = false;
        OrdersService.findAllOrders(page, pageSize).then(result => {
            if (!cancelled) {
                setOrders(result);
            }
        });
        return () => {
            cancelled = true;
        };
    }, [page]);

    async function onDelete(order: OrderResponse) {
        if (!isAdmin) return;
        const confirmed = confirm(`Are you sure you want to delete this order: "${order.orderId}"?`);
        if (!confirmed) return;
        await OrdersService.deleteOrder(order.orderId!);
        const updatedOrders = orders.filter(o => o.orderId !== order.orderId);
        setOrders(updatedOrders);
    }

    function goToPreviousPage() {
        if (page === 0) return;
        setPage(page - 1);
    }

    function goToNextPage() {
        if (orders.length < pageSize) return;
        setPage(page + 1);
    }

    return (
        <div className="row justify-content-center">
            <div className="col-12 col-lg-10">
                <div className="d-flex justify-content-between align-items-center mb-3">
                    <h2 className="mb-0">Orders</h2>
                    <span />
                </div>
                <div className="accordion" id="ordersAccordion">
                    {orders.map(order => {
                        const itemCount = order.orderItems ? order.orderItems.length : 0;
                        const collapseId = `order-collapse-${order.orderId}`;
                        const headingId = `order-heading-${order.orderId}`;
                        return (
                            <div className="accordion-item" key={order.orderId}>
                                <h2 className="accordion-header" id={headingId}>
                                    <button className="accordion-button collapsed" type="button" data-bs-toggle="collapse" data-bs-target={`#${collapseId}`} aria-expanded="false" aria-controls={collapseId}>
                                        <span className="me-2">Order #{order.orderId}</span>
                                        <span className="me-2">· User: {order.userId}</span>
                                        <span className="me-2">· Items: {itemCount}</span>
                                        <span className="ms-auto">Total: {order.totalPrice} €</span>
                                    </button>
                                </h2>
                                <div id={collapseId} className="accordion-collapse collapse" aria-labelledby={headingId} data-bs-parent="#ordersAccordion">
                                    <div className="accordion-body">
                                        {order.orderItems && order.orderItems.length > 0 ? (
                                            <table className="table table-sm align-middle text-center mb-3">
                                                <thead>
                                                <tr>
                                                    <th scope="col" className="text-start">Product</th>
                                                    <th scope="col">Product ID</th>
                                                    <th scope="col">Quantity</th>
                                                    <th scope="col">Price</th>
                                                    <th scope="col">Subtotal</th>
                                                </tr>
                                                </thead>
                                                <tbody>
                                                {order.orderItems.map(item => {
                                                    const price = item.price ?? 0;
                                                    const quantity = item.quantity ?? 0;
                                                    const subtotal = price * quantity;
                                                    return (
                                                        <tr key={item.productId}>
                                                            <td className="text-start">{item.productName}</td>
                                                            <td>{item.productId}</td>
                                                            <td>{quantity}</td>
                                                            <td>{price}</td>
                                                            <td>{subtotal}</td>
                                                        </tr>
                                                    );
                                                })}
                                                </tbody>
                                            </table>
                                        ) : (
                                            <p className="text-muted mb-3">No items in this order.</p>
                                        )}
                                        <div className="d-flex justify-content-between align-items-center">
                                            <span>Total: {order.totalPrice} €</span>
                                            {isAdmin && (
                                                <button type="button" className="btn btn-outline-danger btn-sm" onClick={() => onDelete(order)}>Delete order</button>
                                            )}
                                        </div>
                                    </div>
                                </div>
                            </div>
                        );
                    })}
                    {orders.length === 0 && <p className="text-muted">No orders found.</p>}
                </div>
                <PaginationBar page={page} canGoPrevious={page > 0} canGoNext={orders.length >= pageSize} onPrevious={goToPreviousPage} onNext={goToNextPage}/>
            </div>
        </div>
    );
}

export default OrdersView;
