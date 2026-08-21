import { useEffect, useState } from "react";

function DateTimeCard() {
  const [currentTime, setCurrentTime] = useState(new Date());

  useEffect(() => {
    const timer = setInterval(() => {
      setCurrentTime(new Date());
    }, 1000);

    return () => {
      clearInterval(timer);
    };
  }, []);

  const date = currentTime
    .toLocaleDateString("zh-TW", {
      year: "numeric",
      month: "2-digit",
      day: "2-digit",
    })
    .replace(/\//g, "/");

  const time = currentTime.toLocaleTimeString("en-GB", {
    hour12: false,
  });

  return (
    <div className="card h-100 shadow-sm">
      <div className="card-body">
        <h6 className="card-subtitle mb-3 text-body-secondary">Current Time</h6>

        <div className="fs-4 fw-semibold">{date}</div>

        <div className="fs-5 text-body-secondary">{time}</div>
      </div>
    </div>
  );
}

export default DateTimeCard;
