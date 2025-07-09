import { useState, useEffect } from "react";
import "../../styles/TicketCounter.css";

const TicketCounter = () => {
  const [user, setUser] = useState(JSON.parse(localStorage.getItem("user")));
  const [timeLeft, setTimeLeft] = useState("");

  useEffect(() => {
    if (!user || !user.nextTicketRechargeTime) return;

    const intervalId = setInterval(() => {
      const now = new Date();
      const rechargeTime = new Date(user.nextTicketRechargeTime);
      const difference = rechargeTime - now;

      if (difference > 0) {
        const hours = Math.floor((difference / (1000 * 60 * 60)) % 24);
        const minutes = Math.floor((difference / 1000 / 60) % 60);
        const seconds = Math.floor((difference / 1000) % 60);
        setTimeLeft(
          `${String(hours).padStart(2, "0")}:${String(minutes).padStart(
            2,
            "0"
          )}:${String(seconds).padStart(2, "0")}`
        );
      } else {
        setTimeLeft("Pronto!");
        // Qui potresti aggiungere la logica per aggiornare i ticket dell'utente
        clearInterval(intervalId);
      }
    }, 1000);

    return () => clearInterval(intervalId);
  }, [user]);

  if (!user) return null;

  return (
    <div className="ticket-counter-box">
      <div className="ticket-info">
        <i className="bi bi-ticket-detailed-fill ticket-icon"></i>
        <span className="ticket-amount">{user.filmTickets}</span>
      </div>
      <div className="timer-info">{timeLeft}</div>
    </div>
  );
};

export default TicketCounter;
