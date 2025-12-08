import { type FormEvent, useState } from "react";
import { AuthService, OpenAPI, type LoginResponse } from "../../generated-api";
import type { AuthState } from "../../models/AuthCredentials.ts";

type LoginPageProps = {
    onLogin: (auth: AuthState) => void;
};

function LoginPage({ onLogin }: LoginPageProps) {
    const [username, setUsername] = useState("");
    const [password, setPassword] = useState("");
    const [error, setError] = useState<string | null>(null);
    const [loading, setLoading] = useState(false);

    async function handleSubmit(event: FormEvent) {
        event.preventDefault();
        setError(null);
        setLoading(true);

        OpenAPI.USERNAME = username;
        OpenAPI.PASSWORD = password;

        try {
            const loginResponse: LoginResponse = await AuthService.login();
            onLogin({
                username: loginResponse.username ?? username,
                password,
                roles: loginResponse.roles ?? []
            });
        } catch {
            setError("Login failed. Please check your credentials.");
            OpenAPI.USERNAME = undefined;
            OpenAPI.PASSWORD = undefined;
        } finally {
            setLoading(false);
        }
    }

    return (
        <div className="container d-flex justify-content-center align-items-center vh-100">
            <div className="card p-4">
                <h2 className="mb-3 text-center">OrderHub Login</h2>
                {error && <div className="alert alert-danger mb-3">{error}</div>}
                <form onSubmit={handleSubmit}>
                    <div className="mb-3">
                        <label className="form-label">Username</label>
                        <input type="text" className="form-control" value={username} onChange={(event) => setUsername(event.target.value)} required />
                    </div>
                    <div className="mb-3">
                        <label className="form-label">Password</label>
                        <input type="password" className="form-control" value={password} onChange={(event) => setPassword(event.target.value)} required />
                    </div>
                    <button type="submit" className="btn btn-primary w-100" disabled={loading}>{loading ? "Login..." : "Login"}</button>
                </form>
            </div>
        </div>
    );
}

export default LoginPage;
