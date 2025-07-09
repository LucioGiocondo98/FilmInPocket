import { Container } from "react-bootstrap";
import AuthForm from "./components/auth/AuthForm";
import "./App.css"; // File CSS per stili specifici di App, se necessario (ora vuoto)

function App() {
  return (
    <main>
      <Container
        className="d-flex align-items-center justify-content-center"
        style={{ minHeight: "100vh" }}
      >
        {/* Mostra sempre il form di autenticazione */}
        <AuthForm />
      </Container>
    </main>
  );
}

export default App;
