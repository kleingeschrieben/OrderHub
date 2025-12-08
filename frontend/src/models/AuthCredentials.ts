export type AuthCredentials = {
    username: string;
    password: string;
    roles: string[];
};

export type AuthState = AuthCredentials | null;
