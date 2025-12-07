import { useEffect, useState } from "react"
import {
    OpenAPI,
    OrderControllerService, type OrderItemResponse,
    type OrderResponse,
    type ProductResponse,
    ProductsService
} from "./generated-api";

function App() {
    const [products, setProducts] = useState<ProductResponse[]>([])
    const [orders, setOrders] = useState<OrderResponse[]>([])
    const [loading, setLoading] = useState(true)

    useEffect(() => {
        OpenAPI.BASE = ""
        OpenAPI.USERNAME = "admin"
        OpenAPI.PASSWORD = "admin"

        async function load() {
            try {
                const p = await ProductsService.findAllProducts(0, 50)
                const o = await OrderControllerService.findAllOrders(0, 50)
                setProducts(p ?? [])
                setOrders(o ?? [])
            } finally {
                setLoading(false)
            }
        }

        load()
    }, [])

    if (loading) {
        return (
            <div className="container mt-5">
                <h1>OrderHub</h1>
                <p>Loading...</p>
            </div>
        )
    }

    return (
        <div className="container mt-5">
            <h1 className="mb-4">OrderHub</h1>

            <h2 className="mb-3">Products</h2>
            <table className="table table-bordered table-striped mb-5">
                <thead>
                <tr>
                    <th>Id</th>
                    <th>Name</th>
                    <th>Price</th>
                    <th>Stock</th>
                </tr>
                </thead>
                <tbody>
                {products.map((p) => (
                    <tr key={p.id}>
                        <td>{p.id}</td>
                        <td>{p.name}</td>
                        <td>{p.price}</td>
                        <td>{p.stock}</td>
                    </tr>
                ))}
                </tbody>
            </table>

            <h2 className="mb-3">Orders</h2>
            <table className="table table-bordered table-striped">
                <thead>
                <tr>
                    <th>Order Id</th>
                    <th>User</th>
                    <th>Total Price</th>
                    <th>Items</th>
                </tr>
                </thead>
                <tbody>
                {orders.map((o) => (
                    <tr key={o.orderId}>
                        <td>{o.orderId}</td>
                        <td>{o.userId}</td>
                        <td>{o.totalPrice}</td>
                        <td>
                            {o.orderItems && o.orderItems.length > 0 ? (
                                <ul className="mb-0">
                                    {o.orderItems.map((item: OrderItemResponse, i) => (
                                        <li key={i}>
                                            {item.productName} x {item.quantity} ({item.price})
                                        </li>
                                    ))}
                                </ul>
                            ) : (
                                "No items"
                            )}
                        </td>
                    </tr>
                ))}
                </tbody>
            </table>
        </div>
    )
}

export default App
