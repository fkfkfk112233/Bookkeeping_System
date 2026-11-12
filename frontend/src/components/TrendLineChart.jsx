import {
  Chart as ChartJS,
  CategoryScale,
  LinearScale,
  PointElement,
  LineElement,
  Tooltip,
  Legend,
  Filler,
} from "chart.js";
import { Line } from "react-chartjs-2";

ChartJS.register(
  CategoryScale,
  LinearScale,
  PointElement,
  LineElement,
  Tooltip,
  Legend,
  Filler,
);

function TrendLineChart({ data = [], label, valueKey = "amount", color = "#4F7DF3" }) {
  const chartData = {
    labels: data.map((item) => item.label),
    datasets: [
      {
        label,
        data: data.map((item) => item[valueKey]),
        borderColor: color,
        backgroundColor: (context) => {
          const { chart } = context;
          const { ctx, chartArea } = chart;
          if (!chartArea) return `${color}24`;

          const gradient = ctx.createLinearGradient(0, chartArea.top, 0, chartArea.bottom);
          gradient.addColorStop(0, `${color}55`);
          gradient.addColorStop(1, `${color}00`);
          return gradient;
        },
        pointBackgroundColor: color,
        pointBorderColor: "#FFFFFF",
        pointBorderWidth: 2,
        pointRadius: 4,
        pointHoverRadius: 6,
        borderWidth: 3,
        tension: 0.35,
        fill: true,
      },
    ],
  };

  const options = {
    responsive: true,
    maintainAspectRatio: false,
    interaction: { intersect: false, mode: "index" },
    plugins: {
      legend: { display: true, labels: { usePointStyle: true } },
    },
    scales: {
      x: { grid: { display: false }, ticks: { color: "#7b879c" } },
      y: {
        beginAtZero: true,
        grid: { color: "rgba(70, 88, 125, 0.09)" },
        ticks: { color: "#7b879c" },
      },
    },
  };

  return (
    <div className="chart-container">
      <Line data={chartData} options={options} />
    </div>
  );
}

export default TrendLineChart;
