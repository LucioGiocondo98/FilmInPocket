import { useState } from "react";
import { Form, Button, Alert, Card, Row, Col } from "react-bootstrap";
import "../../styles/AuthForm.css";

const AuthForm = () => {
  const [isLogin, setIsLogin] = useState(true);
  const [username, setUsername] = useState("");
  const [email, setEmail] = useState("");
  const [password, setPassword] = useState("");
  const [error, setError] = useState("");
  const [isClapping, setIsClapping] = useState(false);

  const API_URL = "http://localhost:8080";

  // Funzione per avviare l'animazione
  const handleClap = () => {
    if (isClapping) return;
    setIsClapping(true);
    setTimeout(() => {
      setIsClapping(false);
    }, 400); // La durata deve corrispondere a quella dell'animazione CSS
  };

  // Funzione per gestire l'invio del form
  const handleSubmit = (e) => {
    e.preventDefault();
    handleClap(); // <<--- L'ANIMAZIONE PARTE QUI!

    // La logica di login/registrazione procede immediatamente dopo
    if (isLogin) {
      handleLogin();
    } else {
      handleRegister();
    }
  };

  const handleLogin = () => {
    const credentials = { username, password };
    fetch(`${API_URL}/auth/login`, {
      method: "POST",
      headers: { "Content-Type": "application/json" },
      body: JSON.stringify(credentials),
    })
      .then(handleResponse)
      .then((data) => {
        localStorage.setItem("accessToken", data.accessToken);
        localStorage.setItem("user", JSON.stringify(data.user));
        alert("Login effettuato con successo!");
      })
      .catch((err) => {
        setError(err.message);
        console.error(err);
      });
  };

  const handleRegister = () => {
    const userData = { username, email, password };
    fetch(`${API_URL}/auth/register`, {
      method: "POST",
      headers: { "Content-Type": "application/json" },
      body: JSON.stringify(userData),
    })
      .then(handleResponse)
      .then(() => {
        alert("Registrazione completata! Ora puoi effettuare il login.");
        setIsLogin(true);
        setError("");
      })
      .catch((err) => {
        setError(err.message);
        console.error(err);
      });
  };

  const handleResponse = (response) => {
    if (!response.ok) {
      return response.json().then((err) => {
        throw new Error(err.message || "Operazione fallita");
      });
    }
    if (response.status === 201 || response.status === 204) {
      return null;
    }
    return response.json();
  };

  const toggleForm = () => {
    setIsLogin(!isLogin);
    setError("");
  };

  return (
    <div className="clapperboard">
      {/* MODIFICA: Rimosso l'onClick da qui */}
      <div className={`clapper ${isClapping ? "clapping" : ""}`}></div>

      <div className="board">
        <div className="clapper-static"></div>
        <Card bg="transparent" border="0" text="white">
          <Card.Body>
            <h3 className="form-title">
              {isLogin ? "FilmInPocket" : "Registrati"}
            </h3>

            {error && <Alert variant="danger">{error}</Alert>}

            <Form onSubmit={handleSubmit}>
              <Form.Group className="mb-3">
                <Form.Control
                  type="text"
                  placeholder="Username"
                  className="form-control-clapper"
                  value={username}
                  onChange={(e) => setUsername(e.target.value)}
                  required
                />
              </Form.Group>

              {!isLogin && (
                <Form.Group className="mb-3">
                  <Form.Control
                    type="email"
                    placeholder="Email"
                    className="form-control-clapper"
                    value={email}
                    onChange={(e) => setEmail(e.target.value)}
                    required
                  />
                </Form.Group>
              )}

              <Form.Group className="mb-3">
                <Form.Control
                  type="password"
                  placeholder="Password"
                  className="form-control-clapper"
                  value={password}
                  onChange={(e) => setPassword(e.target.value)}
                  required
                />
              </Form.Group>

              {/* --- INIZIO MODIFICHE PER CENTRARE IL BOTTONE --- */}
              <Row>
                <Col className="d-flex justify-content-center">
                  <Button
                    type="submit"
                    className="action-button w-50"
                    style={{ backgroundColor: "#d4a24d", border: "none" }}
                  >
                    {isLogin ? "Azione!" : "Registrati"}
                  </Button>
                </Col>
              </Row>
              {/* --- FINE MODIFICHE --- */}
            </Form>

            <div className="toggle-text">
              {isLogin ? "Non hai un account? " : "Hai già un account? "}
              <span onClick={toggleForm} className="toggle-link">
                {isLogin ? "Registrati" : "Accedi"}
              </span>
            </div>
          </Card.Body>
        </Card>
      </div>
    </div>
  );
};

export default AuthForm;
