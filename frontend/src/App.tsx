import { useState } from "react";
import LoginPage from "./LoginPage";

type AuthState = {
    username: string;
    password: string;
} | null;

function App() {
    const [auth, setAuth] = useState<AuthState>(null);

    if (!auth) {
        return <LoginPage onLogin={setAuth} />;
    }

    return (
        <div className="container mt-4">
            <h1 className="mb-3">OrderHub</h1>
            <p>
                Angemeldet als <strong>{auth.username}</strong>
            </p>
        </div>
    );
}

export default App;
