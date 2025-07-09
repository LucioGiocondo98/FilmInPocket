import { Navbar, Container, Nav, Offcanvas } from "react-bootstrap";

const TopNavbar = () => {
  return (
    <Navbar
      bg="dark"
      variant="dark"
      expand={false}
      fixed="top"
      className="top-navbar"
    >
      <Container fluid>
        <Navbar.Brand
          href="#"
          style={{ fontFamily: "Lobster, cursive", fontSize: "1.8rem" }}
        >
          FilmInPocket
        </Navbar.Brand>
        <Navbar.Toggle aria-controls="offcanvasNavbar" />
        <Navbar.Offcanvas
          id="offcanvasNavbar"
          aria-labelledby="offcanvasNavbarLabel"
          placement="end"
          className="bg-dark text-white"
        >
          <Offcanvas.Header closeButton closeVariant="white">
            <Offcanvas.Title id="offcanvasNavbarLabel">Menu</Offcanvas.Title>
          </Offcanvas.Header>
          <Offcanvas.Body>
            <Nav className="justify-content-end flex-grow-1 pe-3">
              <Nav.Link href="/profilo">Profilo</Nav.Link>
              <Nav.Link href="/impostazioni">Impostazioni</Nav.Link>
              <Nav.Link href="/logout">Logout</Nav.Link>
            </Nav>
          </Offcanvas.Body>
        </Navbar.Offcanvas>
      </Container>
    </Navbar>
  );
};

export default TopNavbar;
