
import mysql.connector
import pandas as pd
import matplotlib.pyplot as plt

# Connect to MySQL database
con = mysql.connector.connect(
    host="localhost",
    user="root",
    password="YOUR_MYSQL_PASSWORD",
    database="movie_theatre_db"
)

# Fetch movie-wise box office details
query = """
SELECT
    m.title AS movie_name,
    COUNT(b.booking_id) AS total_bookings,
    SUM(b.number_of_tickets) AS tickets_sold,
    SUM(p.amount) AS revenue
FROM payments p
JOIN bookings b ON p.booking_id = b.booking_id
JOIN shows s ON b.show_id = s.show_id
JOIN movies m ON s.movie_id = m.movie_id
WHERE p.payment_status = 'Paid'
AND b.booking_status = 'Confirmed'
GROUP BY m.movie_id, m.title
"""

df = pd.read_sql(query, con)

print("\n===== BOX OFFICE ANALYSIS =====")
print(df)

if not df.empty:

    print("\nTotal Revenue: Rs.", df["revenue"].sum())
    print("Total Tickets Sold:", df["tickets_sold"].sum())

    df.plot(
        x="movie_name",
        y="revenue",
        kind="bar",
        legend=False,
        title="Movie-wise Box Office Revenue"
    )

    plt.xlabel("Movie Name")
    plt.ylabel("Revenue (Rs.)")
    plt.tight_layout()
    plt.savefig("movie_revenue.png")
    plt.show()

else:
    print("No paid confirmed bookings found.")

con.close()
